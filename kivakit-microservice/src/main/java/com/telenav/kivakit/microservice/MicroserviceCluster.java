package com.telenav.kivakit.microservice;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.IdentitySet;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.thread.ReentrancyTracker;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperConnection;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperSettingsStore;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.primitive.Ints.parseInt;
import static com.telenav.kivakit.core.thread.ReentrancyTracker.Reentrancy.ENTERED;
import static com.telenav.kivakit.microservice.MicroserviceClusterMember.localClusterMemberInstanceIdentifier;
import static com.telenav.kivakit.network.core.Host.parseHost;
import static com.telenav.third.party.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

/**
 * <p>
 * Manages members of a cluster by storing {@link MicroserviceClusterMember} objects with associated user data in an
 * ephemeral, sequential Apache Zookeeper store. As members join and leave the cluster, the subclass is notified and
 * leader elections take place.
 * </p>
 *
 * <p><b>Starting Up</b></p>
 *
 * <p>
 * To join this cluster, call {@link #join(MicroserviceClusterMember)}, passing in the user-defined data you wish to
 * associate with your {@link MicroserviceClusterMember}. A member should call this method to join the cluster in
 * {@link Microservice#onRun()}.
 * </p>
 *
 * <p><b>As Members Join and Leave</b></p>
 *
 * <p>
 * When member objects are added to Zookeeper, this indicates that a new member is joining the cluster, and
 * {@link #onJoin(MicroserviceClusterMember)} will be called. When member objects are removed from Zookeeper, a member
 * is leaving the cluster and {@link #onLeave(MicroserviceClusterMember)} will be called. The {@link #members()} method
 * yields the members of this cluster.
 * </p>
 *
 * <p><b>Leader Elections</b></p>
 *
 * <p>
 * At any given time, this cluster has an elected leader. The leader is determined by the sort order of
 * {@link MicroserviceClusterMember}s according to the {@link Comparable} interface implementation. The comparison is
 * implemented by comparing the identifiers returned by {@link MicroserviceClusterMember#identifier()}. Each identifier
 * is composed of the DNS name of the member and the process identifier of the member (multiple members can run on a
 * single host). For example:
 * </p>
 *
 * <pre>Jonathan-iMac.local#83874</pre>
 *
 * <p><b>Membership</b></p>
 *
 * <ul>
 *     <li>{@link #join(MicroserviceClusterMember)}</li>
 *     <li>{@link #leader()}</li>
 *     <li>{@link #leave()}</li>
 *     <li>{@link #loadMembers()}</li>
 *     <li>{@link #members()}</li>
 *     <li>{@link #onJoin(MicroserviceClusterMember)}</li>
 *     <li>{@link #onLeave(MicroserviceClusterMember)}</li>
 *     <li>{@link #thisMember()}</li>
 * </ul>
 *
 * @param <Member> The type of member information to store for each cluster member
 * @author jonathanl (shibo)
 * @see MicroserviceClusterMember
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MicroserviceCluster<Member> extends BaseComponent
{
    /** The current cluster leader as of the last election by {@link #electLeader()} */
    private MicroserviceClusterMember<Member> leader;

    /** The members of this cluster as of the last time that {@link #loadMembers()} was called */
    private ObjectList<MicroserviceClusterMember<Member>> members;

    /** Prevent reentrancy during member loading */
    private final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /**
     * Joins this cluster with the given member data. Keeps trying to join the cluster when disconnected.
     *
     * @param member The member data for this cluster member
     * @return True if the member has signed up to join the cluster when Zookeeper connects. If there is no
     * ZookeeperConnection.Settings object (defined in the deployment folder), no attempt will be made to join the
     * cluster and this method will return false.
     */
    public boolean join(MicroserviceClusterMember<Member> member)
    {
        // Get any settings object (there will be a settings object if the deployment defines it),
        var settings = lookup(ZookeeperConnection.Settings.class);
        if (settings != null)
        {
            // and whenever we connect to Zookeeper in the future,
            require(ZookeeperConnection.class).onConnection(() ->
            {
                // force the loading of existing members,
                loadMembers();

                // then add ourselves as a member.
                store().save(new SettingsObject(member.data(), localClusterMemberInstanceIdentifier()));
            });

            return true;
        }

        return false;
    }

    /**
     * Returns the leader of this cluster
     */
    public MicroserviceClusterMember<Member> leader()
    {
        return leader;
    }

    /**
     * Leaves this cluster and removes the associated member data for this cluster node
     */
    public void leave()
    {
        if (isConnected())
        {
            store().delete(new SettingsObject(thisMember().data(), localClusterMemberInstanceIdentifier()));
        }
        else
        {
            warning("Cannot leave cluster: Zookeeper is not connected");
        }
    }

    /**
     * Returns the members of this cluster at the time this method is called
     */
    public boolean loadMembers()
    {
        try
        {
            if (reentrancy.enter() == ENTERED)
            {
                var loaded = new IdentitySet<MicroserviceClusterMember<Member>>();

                if (isConnected())
                {
                    // Force the store to reload from Zookeeper,
                    store().reload();

                    // then go through the indexed objects
                    for (var settingsObject : store().objects())
                    {
                        // and add each one as a cluster member
                        var child = settingsObject.identifier().instance().enumIdentifier();
                        var flat = store().root().withChild(child.name());
                        var path = store().unflatten(flat);
                        loaded.add(member(path, settingsObject));
                    }
                }
                else
                {
                    warning("Cannot load cluster members: Zookeeper is not connected");
                }

                var newMembers = list(loaded);
                var updated = !newMembers.equals(members);
                members = newMembers;
                return updated;
            }

            return false;
        }
        finally
        {
            reentrancy.exit();
        }
    }

    /**
     * Returns the members of this cluster as of the last call to {@link #loadMembers()}
     */
    public ObjectList<MicroserviceClusterMember<Member>> members()
    {
        return members;
    }

    /**
     * Returns the cluster member for this process and host
     */
    public MicroserviceClusterMember<Member> thisMember()
    {
        if (isConnected())
        {
            for (var member : members())
            {
                if (member.isThis())
                {
                    return member;
                }
            }
        }
        else
        {
            warning("Cannot get this member: Zookeeper is not connected");
        }

        return null;
    }

    /**
     * Called when a member joins this cluster
     */
    protected void onJoin(MicroserviceClusterMember<Member> member)
    {
    }

    /**
     * Called when a member leaves this cluster
     */
    protected void onLeave(MicroserviceClusterMember<Member> member)
    {
    }

    /**
     * Choose which member is the leader and set isElected accordingly
     */
    private void electLeader()
    {
        var members = members().sorted();
        if (members.isNonEmpty())
        {
            var first = members.first();
            if (leader == null || !leader.equals(first))
            {
                members.forEach(at -> at.elect(false));
                first.elect(true);
                leader = first;

                announce("Elected as leader: $", first);
                showMembers(members);
            }
        }
    }

    private boolean isConnected()
    {
        return require(ZookeeperConnection.class).isConnected();
    }

    @NotNull
    private MicroserviceClusterMember<Member> member(StringPath path,
                                                     SettingsObject settings)
    {
        var parts = path.last().split("#");

        return new MicroserviceClusterMember<>(
                parseHost(this, parts[0]),
                parseInt(this, parts[1]),
                parseInt(this, parts[2]),
                settings.object());
    }

    private void showMembers(ObjectList<MicroserviceClusterMember<Member>> members)
    {
        var output = new StringList();
        for (var member : members)
        {
            output.add(member.identifier()
                    + (member.isThis() ? " [this]" : "")
                    + (member.isLeader() ? " [leader]" : ""));
        }

        announce(output.titledBox("Cluster Members"));
    }

    private ZookeeperSettingsStore store()
    {
        return store;
    }

    /** Zookeeper settings store used to track cluster members */
    private final ZookeeperSettingsStore store = listenTo(new ZookeeperSettingsStore(EPHEMERAL_SEQUENTIAL, new GsonObjectSerializer())
    {
        @Override
        protected void onSettingsDeleted(StringPath path, SettingsObject settings)
        {
            if (loadMembers())
            {
                var member = member(path, settings);
                announce("Leaving cluster: $", member.identifier());
                onLeave(member);
                remove(settings);
                electLeader();
            }
        }

        @Override
        protected void onSettingsUpdated(StringPath path, SettingsObject settings)
        {
            if (loadMembers())
            {
                var member = member(path, settings);
                add(settings);
                register(settings.object(), settings.identifier().instance());
                announce("Joining cluster: $", member.identifier());
                onJoin(member);
                electLeader();
            }
        }
    });
}

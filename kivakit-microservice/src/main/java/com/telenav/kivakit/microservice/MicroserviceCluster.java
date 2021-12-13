package com.telenav.kivakit.microservice;

import com.telenav.kivakit.collections.set.IdentitySet;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.primitives.Ints;
import com.telenav.kivakit.kernel.language.threading.status.ReentrancyTracker;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperSettingsStore;
import org.jetbrains.annotations.NotNull;

import static org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

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
 * associate with your {@link MicroserviceClusterMember}. A member should call this method to join the cluster in {@link
 * Microservice#onRun()}.
 * </p>
 *
 * <p><b>As Members Join and Leave</b></p>
 *
 * <p>
 * When member objects are added to Zookeeper, this indicates that a new member is joining the cluster, and {@link
 * #onJoin(MicroserviceClusterMember)} will be called. When member objects are removed from Zookeeper, a member is
 * leaving the cluster and {@link #onLeave(MicroserviceClusterMember)} will be called. The {@link #members()} method
 * yields the members of this cluster.
 * </p>
 *
 * <p><b>Leader Elections</b></p>
 *
 * <p>
 * At any given time, this cluster has an elected leader. The leader is determined by the sort order of {@link
 * MicroserviceClusterMember}s according to the {@link Comparable} interface implementation. The comparison is
 * implemented by comparing the identifiers returned by {@link MicroserviceClusterMember#identifier()}. Each identifier
 * is composed of the DNS name of the member and the process identifier of the member (multiple members can run on a
 * single host). For example:
 * </p>
 *
 * <pre>Jonathans-iMac.local#83874</pre>
 *
 * @param <Member> The type of member information to store for each cluster member
 * @author jonathanl (shibo)
 * @see MicroserviceClusterMember
 */
public class MicroserviceCluster<Member> extends BaseComponent
{
    /** Tracks reentrancy to the onSettingsUpdated method to ensure that it is not called recursively */
    private final ReentrancyTracker reentrancy = new ReentrancyTracker();

    /** Zookeeper settings store used to track cluster members */
    private final Lazy<ZookeeperSettingsStore> store = Lazy.of(() -> listenTo(new ZookeeperSettingsStore(EPHEMERAL_SEQUENTIAL)
    {
        @Override
        protected void onSettingsDeleted(StringPath path, SettingsObject settings)
        {
            var member = member(path, settings);
            announce("Leaving cluster: $", member.identifier());
            onLeave(member);
            unindex(settings);
            electLeader();
        }

        @Override
        protected void onSettingsUpdated(StringPath path, SettingsObject settings)
        {
            try
            {
                if (!reentrancy.enter())
                {
                    var member = member(path, settings);
                    index(settings);
                    announce("Joining cluster: $", member.identifier());
                    onJoin(member);
                    electLeader();
                }
            }
            finally
            {
                reentrancy.exit();
            }
        }
    }));

    /**
     * Joins this cluster with the given member data
     *
     * @param member The member data for this cluster member
     */
    public void join(MicroserviceClusterMember<Member> member)
    {
        // Force loading of existing members,
        members();

        // then add ourselves as a member.
        store().save(new SettingsObject(member.data(), instanceIdentifier()));
    }

    /**
     * Leaves this cluster and removes the associated member data for this cluster node
     */
    public void leave()
    {
        store().delete(new SettingsObject(thisMember().data(), instanceIdentifier()));
    }

    /**
     * @return The members of this cluster at the time this method is called
     */
    public ObjectList<MicroserviceClusterMember<Member>> members()
    {
        var members = new IdentitySet<MicroserviceClusterMember<Member>>();

        // Force the store to reload from Zookeeper,
        store().forceLoad();

        // then go through the indexed objects
        for (var settingsObject : store().indexed())
        {
            // and add each one as a cluster member
            var child = settingsObject.identifier().instance().identifier();
            var flat = store().root().withChild(child);
            var path = store().unflatten(flat);
            members.add(member(path, settingsObject));
        }

        return ObjectList.objectList(members);
    }

    /**
     * @return The member for this process and host
     */
    public MicroserviceClusterMember<Member> thisMember()
    {
        for (var member : members())
        {
            if (member.isThis())
            {
                return member;
            }
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

            var elected = true;
            for (var member : members)
            {
                member.elect(elected);
                if (elected)
                {
                    trace("Elected as leader: $", first);
                }
                elected = false;
            }

            showMembers(members);
        }
    }

    @NotNull
    private InstanceIdentifier instanceIdentifier()
    {
        return InstanceIdentifier.of(Host.local().dnsName() + "#" + OperatingSystem.get().processIdentifier() + "#");
    }

    @NotNull
    private MicroserviceClusterMember<Member> member(final StringPath path,
                                                     final SettingsObject settings)
    {
        var parts = path.last().split("#");

        return new MicroserviceClusterMember<>(
                Host.parse(this, parts[0]),
                Ints.parse(this, parts[1]),
                Ints.parse(this, parts[2]),
                settings.object());
    }

    private void showMembers(final ObjectList<MicroserviceClusterMember<Member>> members)
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
        return store.get();
    }


}

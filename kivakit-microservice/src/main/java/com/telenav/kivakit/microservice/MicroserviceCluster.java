package com.telenav.kivakit.microservice;

import com.telenav.kivakit.collections.set.IdentitySet;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.primitives.Ints;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperSettingsStore;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;

/**
 * <p>
 * Manages members of a cluster by storing {@link MicroserviceClusterMember} objects with associated user data in an
 * ephemeral Apache Zookeeper store. As members join and leave the cluster, the subclass is notified.
 * </p>
 *
 * <p><b>Starting Up</b></p>
 *
 * <p>
 * To join this cluster, call {@link #join(MicroserviceClusterMember)}, passing in the user-defined data you wish to
 * associate with your {@link MicroserviceClusterMember}. A member should call this method to join the cluster in {@link
 * Microservice#onRun()}. The {@link #members()} method yields the members of this cluster.
 * </p>
 *
 * <p><b>As Members Join and Leave</b></p>
 *
 * <p>
 * When member objects are added to Zookeeper, a new member is joining the cluster and {@link
 * #onJoin(MicroserviceClusterMember)} will be called. When member objects are removed from Zookeeper, a member is
 * leaving the cluster and {@link #onLeave(MicroserviceClusterMember)} will be called.
 * </p>
 *
 * @param <Member> The type of member information to store for each cluster member
 * @author jonathanl (shibo)
 */
public class MicroserviceCluster<Member> extends BaseComponent
{
    /** Store for ephemeral cluster Member objects in Zookeeper */
    private final Lazy<ZookeeperSettingsStore> store = Lazy.of(() -> listenTo(new ZookeeperSettingsStore(EPHEMERAL)
    {
        @Override
        protected void onSettingsDeleted(StringPath path, SettingsObject settings)
        {
            var member = member(path, settings);
            announce("Leaving cluster: $", member);
            onLeave(member);
        }

        @Override
        protected void onSettingsUpdated(StringPath path, SettingsObject settings)
        {
            var member = member(path, settings);
            announce("Joining cluster: $", member);
            onJoin(member);
        }
    }));

    /**
     * Joins this cluster with the given member data
     *
     * @param member The member data for this cluster member
     */
    public void join(MicroserviceClusterMember<Member> member)
    {
        // Call onJoin() for each member that's already in the cluster we joined,
        for (var at : members())
        {
            if (!at.equals(member))
            {
                onJoin(at);
            }
        }

        // then add ourselves as a member.
        store().save(memberSettings(member, instanceIdentifier()));
    }

    /**
     * Leaves this cluster and removes the associated member data for this cluster node
     */
    public void leave()
    {
        store().delete(memberSettings(new MicroserviceClusterMember<>(null), instanceIdentifier()));
    }

    /**
     * @return The members of this cluster
     */
    public Set<MicroserviceClusterMember<Member>> members()
    {
        var members = new IdentitySet<MicroserviceClusterMember<Member>>();

        for (var at : store().indexed())
        {
            members.add(new MicroserviceClusterMember<>(at.object()));
        }

        return members;
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

    @NotNull
    private InstanceIdentifier instanceIdentifier()
    {
        return InstanceIdentifier.of(Host.local().dnsName() + "#" + OperatingSystem.get().processIdentifier());
    }

    @NotNull
    private MicroserviceClusterMember<Member> member(final StringPath path,
                                                     final SettingsObject settings)
    {
        var parts = path.last().split("#");

        return new MicroserviceClusterMember<>(
                Host.parse(this, parts[0]),
                Ints.parse(this, parts[1]),
                settings.object());
    }

    @NotNull
    private SettingsObject memberSettings(MicroserviceClusterMember<Member> member, InstanceIdentifier instance)
    {
        return new SettingsObject(member.data(), instance);
    }

    private ZookeeperSettingsStore store()
    {
        return store.get();
    }
}

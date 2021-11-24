package com.telenav.kivakit.microservice;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperSettingsStore;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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
 * To join this cluster, call {@link #join(Object)}, passing in the user-defined data you wish to associate with your
 * {@link MicroserviceClusterMember}. A member should call this method to join the cluster in {@link
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
    /** Store for cluster Member objects in Zookeeper */
    private Lazy<ZookeeperSettingsStore> store = Lazy.of(() -> listenTo(new ZookeeperSettingsStore(EPHEMERAL)
    {
        @Override
        protected void onSettingsRemoved(StringPath path, SettingsObject settings)
        {
            onLeave(settings.object());
        }

        @Override
        protected void onSettingsUpdated(StringPath path, SettingsObject settings)
        {
            onJoin(settings.object());
        }
    }));

    /**
     * Joins this cluster with the given member data
     *
     * @param member The member data for this cluster member
     */
    public void join(Member member)
    {
        store().save(settings(member, instanceIdentifier()));
    }

    /**
     * Leaves this cluster and removes the associated member data for this cluster node
     */
    public void leave()
    {
        store().delete(settings(null, instanceIdentifier()));
    }

    /**
     * @return The members of this cluster
     */
    public Set<MicroserviceClusterMember<Member>> members()
    {
        var members = new HashSet<MicroserviceClusterMember<Member>>();

        for (var at : store().indexedSettingsObjects())
        {
            members.add(at.object());
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
        return InstanceIdentifier.of("cluster-member-" + Host.local().name());
    }

    @NotNull
    private SettingsObject settings(Member member, InstanceIdentifier instance)
    {
        return new SettingsObject(new MicroserviceClusterMember<>(Host.local(), member), instance);
    }

    private ZookeeperSettingsStore store()
    {
        return store.get();
    }
}

package com.telenav.kivakit.microservice;

import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperSettingsStore;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages members of a cluster by storing cluster Member objects. When member objects are added, a cluster member is
 * joining the cluster and {@link #onJoin(Object)} is called. When member objects are removed, a cluster member is
 * leaving the cluster and {@link #onLeave(Object)} is called. The {@link #members()} method
 *
 * @param <Member> The type of member information to store for each cluster member
 * @author jonathanl (shibo)
 */
public class MicroserviceCluster<Member>
{
    /** The type of the cluster Member object */
    private final Class<Member> memberType;

    /** Store for cluster Member objects in Zookeeper */
    private final ZookeeperSettingsStore store = new ZookeeperSettingsStore()
    {
        @Override
        @SuppressWarnings("unchecked")
        protected void onSettingsAdded(Object member)
        {
            if (isMemberObject(member))
            {
                onJoin((Member) member);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onSettingsRemoved(Object member)
        {
            if (isMemberObject(member))
            {
                onLeave((Member) member);
            }
        }
    };

    public MicroserviceCluster(Class<Member> memberType)
    {
        this.memberType = memberType;
    }

    public void join(Member member, InstanceIdentifier instance)
    {
        store.add(new SettingsObject(memberType, instance, member));
    }

    @SuppressWarnings("unchecked")
    public Set<Member> members()
    {
        var members = new HashSet<Member>();

        for (var at : store.all())
        {
            if (isMemberObject(at))
            {
                members.add((Member) at);
            }
        }

        return members;
    }

    protected void onJoin(Member instance)
    {

    }

    protected void onLeave(Member instance)
    {
    }

    private boolean isMemberObject(Object member)
    {
        return memberType.isAssignableFrom(member.getClass());
    }
}

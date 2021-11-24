package com.telenav.kivakit.microservice;

import com.telenav.kivakit.network.core.Host;

/**
 * Represents a member of a {@link MicroserviceCluster} with associated user data
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceClusterMember<Data>
{
    /** The host that the cluster member is running on */
    private Host host;

    /** User-defined data */
    private Data data;

    public MicroserviceClusterMember(Host host, Data data)
    {
        this.host = host;
        this.data = data;
    }

    public Data data()
    {
        return data;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MicroserviceClusterMember)
        {
            MicroserviceClusterMember<?> that = (MicroserviceClusterMember<?>) object;
            return host.equals(that.host);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return host.hashCode();
    }

    public Host host()
    {
        return host;
    }
}

package com.telenav.kivakit.microservice;

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.network.core.Host;

/**
 * Represents a member of a {@link MicroserviceCluster} with associated user data
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceClusterMember<Data>
{
    /** This cluster member */
    private static final MicroserviceClusterMember<Object> THIS_CLUSTER_MEMBER = new MicroserviceClusterMember<>(null);

    /** The host that the cluster member is running on */
    private final Host host;

    /** User-defined data */
    private final Data data;

    /** Process number */
    private final int processIdentifier;

    public MicroserviceClusterMember(Host host, int processIdentifier, Data data)
    {
        this.processIdentifier = processIdentifier;
        this.host = host;
        this.data = data;
    }

    public MicroserviceClusterMember(Data data)
    {
        this(Host.local(), OperatingSystem.get().processIdentifier(), data);
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
            return host.dnsName().equals(that.host.dnsName()) && processIdentifier == that.processIdentifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(host, processIdentifier);
    }

    public Host host()
    {
        return host;
    }

    public int processIdentifier()
    {
        return processIdentifier;
    }

    @Override
    public String toString()
    {
        return host.dnsName() + "#" + processIdentifier() + (equals(THIS_CLUSTER_MEMBER) ? " [this]" : "");
    }
}

package com.telenav.kivakit.microservice;

import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.network.core.Host;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a member of a {@link MicroserviceCluster} with associated user data
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceClusterMember<Data> implements Comparable<MicroserviceClusterMember<Data>>
{
    /** This cluster member */
    private static final MicroserviceClusterMember<Object> THIS_CLUSTER_MEMBER = new MicroserviceClusterMember<>(null);

    /** User-defined data */
    private final Data data;

    /** The host that the cluster member is running on */
    private final Host host;

    /** Process number */
    private final int processIdentifier;

    /** True if this member is the cluster leader */
    private boolean isLeader;

    /** The sequence number of this member, designating the order in which it joined the cluster */
    private final int sequenceNumber;

    public MicroserviceClusterMember(Host host, int processIdentifier, int sequenceNumber, Data data)
    {
        this.host = host;
        this.processIdentifier = processIdentifier;
        this.sequenceNumber = sequenceNumber;
        this.data = data;
    }

    /**
     * For debugging and testing
     */
    public MicroserviceClusterMember(Data data)
    {
        this(Host.local(), OperatingSystem.get().processIdentifier(), 0, data);
    }

    @Override
    public int compareTo(@NotNull final MicroserviceClusterMember<Data> that)
    {
        return identifier().compareTo(that.identifier());
    }

    public Data data()
    {
        return data;
    }

    public void elect(final boolean elected)
    {
        this.isLeader = elected;
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

    /**
     * @return The host that this member is running on
     */
    public Host host()
    {
        return host;
    }

    /**
     * @return True if this member is the elected cluster leader
     */
    public boolean isLeader()
    {
        return isLeader;
    }

    /**
     * @return True if this cluster member is running on this host, in this process
     */
    public boolean isThis()
    {
        return host.dnsName().equals(Host.local().dnsName())
                && processIdentifier == OperatingSystem.get().processIdentifier();
    }

    /**
     * @return The process identifier on the {@link #host()} for this member
     */
    public int processIdentifier()
    {
        return processIdentifier;
    }

    @Override
    public String toString()
    {
        return identifier() + (equals(THIS_CLUSTER_MEMBER) ? " [this]" : "");
    }

    @NotNull
    private String identifier()
    {
        return host.dnsName() + "#" + processIdentifier() + "#" + sequenceNumber;
    }
}

package com.telenav.kivakit.microservice;

import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.network.core.Host;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.registry.InstanceIdentifier.instanceIdentifier;
import static com.telenav.kivakit.network.core.LocalHost.localhost;

/**
 * Represents a member of a {@link MicroserviceCluster} with associated user data.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #data()} - The user data for this member</li>
 *     <li>{@link #host()} - The host where this member is running</li>
 *     <li>{@link #identifier()} - The unique identifier for this cluster member</li>
 *     <li>{@link #isLeader()} - True if this member is the cluster leader</li>
 *     <li>{@link #isThis()} - True if this member object is the one running in this process (on this host)</li>
 *     <li>{@link #localClusterMemberInstanceIdentifier()}</li>
 *     <li>{@link #processIdentifier()} - The process on the {@link #host()} where this member is running</li>
 * </ul>
 *
 * <p><b>Leader Elections</b></p>
 *
 * <p>
 * When the {@link MicroserviceCluster} for this member loses a member or is joined by a new member, an election takes
 * place to select a "leader". If this member is elected, {@link #elect(boolean)} will be called with <i>true</i>,
 * and the {@link #isLeader()} method will return true. See {@link MicroserviceCluster} for details on elections.
 * </p>
 *
 * @param <Data> The type of user data for this member
 * @author jonathanl (shibo)
 * @see Host
 * @see MicroserviceCluster
 */
@SuppressWarnings("unused")
public class MicroserviceClusterMember<Data> implements Comparable<MicroserviceClusterMember<Data>>
{
    /**
     * Returns the instance
     */
    @NotNull
    public static InstanceIdentifier localClusterMemberInstanceIdentifier()
    {
        return instanceIdentifier(localhost().dnsName() + "#" + operatingSystem().processIdentifier() + "#");
    }

    /** User-defined data */
    private final Data data;

    /** The host that the cluster member is running on */
    private final Host host;

    /** True if this member is the cluster leader */
    private boolean isLeader;

    /** Process number */
    private final int processIdentifier;

    /** The sequence number of this member, designating the order in which it joined the cluster */
    private final int sequenceNumber;

    /**
     * Create a new cluster member
     *
     * @param host The host
     * @param processIdentifier The process identifier for this member
     * @param sequenceNumber The sequence number, designating order of joining
     * @param data The user-defined data for this member
     */
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
    protected MicroserviceClusterMember(Data data)
    {
        this(localhost(), operatingSystem().processIdentifier(), 0, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull MicroserviceClusterMember<Data> that)
    {
        return identifier().compareTo(that.identifier());
    }

    /**
     * Returns the user data associated with this cluster member
     */
    public Data data()
    {
        return data;
    }

    /**
     * Chooses this member as the cluster leader if elected is true
     *
     * @param elected True to elect this member, false otherwise
     */
    public void elect(boolean elected)
    {
        this.isLeader = elected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MicroserviceClusterMember)
        {
            var that = (MicroserviceClusterMember<?>) object;
            return host.dnsName().equals(that.host.dnsName())
                    && processIdentifier == that.processIdentifier;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return hashMany(host, processIdentifier);
    }

    /**
     * Returns the host that this member is running on
     */
    public Host host()
    {
        return host;
    }

    @NotNull
    public String identifier()
    {
        return host.dnsName() + "#" + processIdentifier();
    }

    /**
     * Returns true if this member is the elected cluster leader
     */
    public boolean isLeader()
    {
        return isLeader;
    }

    /**
     * Returns true if this cluster member is running on this host, in this process
     */
    public boolean isThis()
    {
        return host.dnsName().equals(localhost().dnsName())
                && processIdentifier == operatingSystem().processIdentifier();
    }

    /**
     * Returns the process identifier on the {@link #host()} for this member
     */
    public int processIdentifier()
    {
        return processIdentifier;
    }

    @Override
    public String toString()
    {
        var thisMember = new MicroserviceClusterMember<>(null);
        return identifier() + "#" + sequenceNumber + (equals(thisMember) ? " [this]" : "");
    }
}

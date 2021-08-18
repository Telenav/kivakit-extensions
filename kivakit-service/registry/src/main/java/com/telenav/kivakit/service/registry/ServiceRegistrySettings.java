////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry;

import com.telenav.kivakit.kernel.data.conversion.string.language.VersionConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.core.Port;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Service registry settings. For defaults, see ServiceRegistrySettings.properties below:
 *
 * <ul>
 *     <li>{@link #localServiceRegistryPort(int)}</li>
 *     <li>{@link #networkServiceRegistryPort(Port)}</li>
 *     <li>{@link #portReservationExpirationTime(Duration)}</li>
 *     <li>{@link #restApiPath(String)}</li>
 *     <li>{@link #serviceLeaseRenewalFrequency(Frequency)}</li>
 *     <li>{@link #serviceRegistrationExpirationTime(Duration)}</li>
 *     <li>{@link #serviceRegistryStoreExpirationTime(Duration)}</li>
 *     <li>{@link #version(Version)}</li>
 * </ul>
 *
 * <p><b>ServiceRegistrySettings.properties</b></p>
 *
 * <pre>
 * class = com.telenav.kivakit.service.registry.ServiceRegistrySettings
 *
 * version = 0.9.9-SNAPSHOT
 * rest-api-path = api/v8
 * local-service-registry-port = 23573
 * network-service-registry-port = kivakit.service.registry:23575
 * service-lease-renewal-frequency = 30 seconds
 * service-registration-expiration-time = 2 minutes
 * service-registry-store-expiration-time = 2 hours
 * port-reservation-expiration-time = 2 hours
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistrySettings
{
    /** Path to REST API */
    private String restApiPath;

    /** The version of this build of service registry code */
    private Version version;

    /** The time-to-live of stored data before it is discarded */
    private Duration serviceRegistryStoreExpirationTime;

    /**
     * Ports will not be re-allocated after expiring for this amount of time. This allows a server to lose contact with
     * the registry, losing its registration entry, but when it comes back in contact with the registry it will be able
     * to reclaim its expired port and continue so long as it wasn't out of contact for more than this duration.
     */
    private Duration portReservationExpirationTime;

    /** Registry bindings expire after this amount of time */
    private Duration serviceRegistrationExpirationTime;

    /** The host and port used by the network service registry */
    private Port networkServiceRegistryPort;

    /** The port used by the local service registry */
    private int localServiceRegistryPort;

    /** The frequency at which clients should renew leases */
    private Frequency serviceLeaseRenewalFrequency;

    /**
     * <b>Not public API</b>
     */
    public Port local()
    {
        return port(Host.loopback());
    }

    @KivaKitPropertyConverter(IntegerConverter.class)
    public ServiceRegistrySettings localServiceRegistryPort(final int localServiceRegistryPort)
    {
        this.localServiceRegistryPort = localServiceRegistryPort;
        return this;
    }

    @KivaKitIncludeProperty
    public int localServiceRegistryPort()
    {
        return localServiceRegistryPort;
    }

    /**
     * <b>Not public API</b>
     *
     * @return The service registry for the network (normally some kind of intranet)
     */
    public Port network()
    {
        final var port = JavaVirtualMachine.property
                (
                        "KIVAKIT_NETWORK_SERVICE_REGISTRY_PORT",
                        "kivakit-network-service-registry.mypna.com:23575"
                );

        if (port != null)
        {
            return Port.parse(port);
        }

        return networkServiceRegistryPort();
    }

    @KivaKitIncludeProperty
    public Port networkServiceRegistryPort()
    {
        return networkServiceRegistryPort;
    }

    @KivaKitPropertyConverter(Port.Converter.class)
    public ServiceRegistrySettings networkServiceRegistryPort(final Port networkServiceRegistryPort)
    {
        this.networkServiceRegistryPort = networkServiceRegistryPort;
        return this;
    }

    @KivaKitIncludeProperty
    public Duration portReservationExpirationTime()
    {
        return portReservationExpirationTime;
    }

    @KivaKitPropertyConverter(Duration.Converter.class)
    public ServiceRegistrySettings portReservationExpirationTime(final Duration portReservationExpirationTime)
    {
        this.portReservationExpirationTime = portReservationExpirationTime;
        return this;
    }

    @KivaKitIncludeProperty
    public String restApiPath()
    {
        return restApiPath;
    }

    @KivaKitPropertyConverter
    public ServiceRegistrySettings restApiPath(final String restApiPath)
    {
        this.restApiPath = restApiPath;
        return this;
    }

    @KivaKitIncludeProperty
    public Frequency serviceLeaseRenewalFrequency()
    {
        return serviceLeaseRenewalFrequency;
    }

    @KivaKitPropertyConverter(Frequency.Converter.class)
    public ServiceRegistrySettings serviceLeaseRenewalFrequency(final Frequency serviceRenewalFrequency)
    {
        serviceLeaseRenewalFrequency = serviceRenewalFrequency;
        return this;
    }

    @KivaKitIncludeProperty
    public Duration serviceRegistrationExpirationTime()
    {
        return serviceRegistrationExpirationTime;
    }

    @KivaKitPropertyConverter(Duration.Converter.class)
    public ServiceRegistrySettings serviceRegistrationExpirationTime(final Duration registrationExpirationTime)
    {
        serviceRegistrationExpirationTime = registrationExpirationTime;
        return this;
    }

    @KivaKitIncludeProperty
    public Duration serviceRegistryStoreExpirationTime()
    {
        return serviceRegistryStoreExpirationTime;
    }

    @KivaKitPropertyConverter(Duration.Converter.class)
    public ServiceRegistrySettings serviceRegistryStoreExpirationTime(final Duration serviceRegistryStoreExpirationTime)
    {
        this.serviceRegistryStoreExpirationTime = serviceRegistryStoreExpirationTime;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @KivaKitIncludeProperty
    public Version version()
    {
        return version;
    }

    @KivaKitPropertyConverter(VersionConverter.class)
    public void version(final Version version)
    {
        this.version = version;
    }

    /**
     * <b>Not public API</b>
     */
    Port port(final Host host)
    {
        return host.http(localServiceRegistryPort());
    }
}

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

package com.telenav.kivakit.service.registry.registries;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.collections.map.MultiSet;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.ServiceRegistryUpdater;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRegistry;
import com.telenav.kivakit.service.registry.store.ServiceRegistryStore;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook.Order.FIRST;

/**
 * <b>Not public API</b>
 * <p>
 * Base implementation of the {@link ServiceRegistry} interface for both {@link LocalServiceRegistry} and {@link
 * NetworkServiceRegistry}. This class holds registration information added by clients through the web service API,
 * allowing for registration and lookup of services that are uniquely identified by {@link Port} (which includes the
 * host for the port). Service bindings are saved by {@link ServiceRegistryStore} every few seconds and on application
 * shutdown to ensure that service registration information remains intact during a registry service update or reboot.
 * </p>
 * <p>
 * <b>Construction</b>
 * </p>
 * <p>
 * The constructor for this class takes a first-port parameter, which specifies the first port in the ephemeral range to
 * allocate. Normally this argument is 50,000 which means that ports in the range between 50,000 and 65,535 (inclusive)
 * can be allocated by the service registry if they are free.
 * </p>
 * <p>
 * <b>Service Leases and Port Reservations</b>
 * </p>
 * <p>
 * Calls to the method {@link #register(Service)} initiate a "lease" on a service entry in the registry, which includes
 * exclusive access to a port that is allocated to the service by the registry. This lease will expire in a few minutes,
 * as determined by {@link ServiceRegistrySettings#serviceRegistrationExpirationTime()}, if the client does not call the
 * {@link #renew(Service)} method to renew the lease.
 * </p>
 * <p>
 * When the lease expires, the registry entry will be removed and the port will be marked as "reserved", in case the
 * service comes back on-line within {@link ServiceRegistrySettings#portReservationExpirationTime()}. After this second
 * expiration period, the port can be re-allocated to new clients by the registry.
 * </p>
 * <p>
 * <i>The process of renewing leases is automatically handled by ServiceRegistryClient.</i>
 * </p>
 * <p>
 * <b>API Details</b>
 * <p>
 * For details on the KivaKit service registry API, see {@link ServiceRegistry}.
 *
 * @author jonathanl (shibo)
 * @see ServiceRegistry
 * @see Service
 * @see ServiceType
 * @see Scope
 * @see Application.Identifier
 * @see Port
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlNotPublicApi
@UmlExcludeSuperTypes({ Startable.class })
public abstract class BaseServiceRegistry extends BaseRepeater implements ServiceRegistry, Startable
{
    /** Services by application */
    private MultiSet<Application.Identifier, Service> applicationToServices = new MultiSet<>();

    /** Lock accesses to data structures */
    private transient final ReadWriteLock lock = new ReadWriteLock();

    /** Services by port */
    private Map<Port, Service> portToService = new HashMap<>();

    /** The ports that are currently registered to a service */
    private Set<Integer> registeredPorts = new HashSet<>();

    /** The time each service last renewed its lease */
    private Map<Service, Time> renewedAt = new HashMap<>();

    /** The ports that are still reserved for applications that might come back after losing registration */
    private Map<Integer, Time> reservedPortToExpirationTime = new HashMap<>();

    /** True if the registry has been started and is running */
    private boolean running;

    /** Services by type */
    private MultiSet<ServiceType, Service> serviceTypeToServices = new MultiSet<>();

    /**
     * Store of this registry's data used during registry server deployments and reboots to maintain registrations
     */
    private final transient ServiceRegistryStore store = listenTo(new ServiceRegistryStore());

    private ServiceRegistryUpdater updater;

    public BaseServiceRegistry()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Result<Boolean> addOrUpdate(Service service)
    {
        // Update the service registration by simply re-registering it
        assert service.isBound();

        lock.write(() ->
        {
            // If the service is bound to a port,
            if (service.isBound())
            {
                // update the renewal time
                trace("Renewing lease on service: $", service);
                service.renewedAt(Time.now());
                renewedAt.put(service, Time.now());

                // index the service for retrieval
                trace("Adding service to indexes: $", service);
                registeredPorts.add(service.port().number());
                portToService.put(service.port(), service);
                serviceTypeToServices.replaceValue(service.type(), service);
                applicationToServices.replaceValue(service.application(), service);

                // and if we are a local service registry and the service should be visible
                // outside the local host,
                if (isLocal() && !service.scope().isLocal())
                {
                    // then propagate service information to the network registry.
                    updater.sendUpdate(service);
                }
            }
            else
            {
                warning("Attempted to add/update an unbound service $", service);
            }
        });

        return result(true);
    }

    /**
     * @return All applications that have registered a service with this registry
     */
    @Override
    @NotNull
    public Result<Set<Application.Identifier>> discoverApplications(Scope scope)
    {
        return lock.read(() -> result(applicationToServices.keySet()));
    }

    /**
     * @return Any service running on the given port
     */
    @Override
    public @NotNull Result<Service> discoverPortService(Port port)
    {
        return lock.read(() -> result(portToService.get(port)));
    }

    /**
     * Any service of the given type registered by the given application
     */
    @Override
    public @NotNull Result<Set<Service>> discoverServices(Application.Identifier application,
                                                          ServiceType type)
    {
        return lock.read(() ->
        {
            trace("Discovering $ services of $", type, application);
            var services = new HashSet<Service>();
            discoverServices(application).get().forEach(service ->
            {
                if (service.type().equals(type))
                {
                    services.add(service);
                }
            });
            trace("Discovered: $", services);
            return result(services);
        });
    }

    /**
     * All services of the given type that have been registered with this registry
     */
    @Override
    public @NotNull Result<Set<Service>> discoverServices(ServiceType type)
    {
        return lock.read(() -> result(serviceTypeToServices.getOrEmptySet(type)));
    }

    /**
     * All services registered by the given application
     */
    @Override
    public @NotNull Result<Set<Service>> discoverServices(Application.Identifier application)
    {
        return lock.read(() ->
        {
            trace("Locating services of $", application);
            return result(applicationToServices.getOrEmptySet(application));
        });
    }

    /**
     * @return All services registered with this registry
     */
    @Override
    public @NotNull Result<Set<Service>> discoverServices()
    {
        return lock.read(() ->
        {
            trace("Discovering all services");
            var result = result(services());
            trace("Discovered services: $", result.get());
            return result;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning()
    {
        return running;
    }

    /**
     * @return A new service registry instance loaded from the {@link ServiceRegistryStore} or this registry itself if
     * there is no serialized data to load
     */
    public ServiceRegistry load()
    {
        lock.write(() ->
        {
            try
            {
                // Load any service registry object that was saved to the store,
                trace("Loading service registry");
                var loaded = (BaseServiceRegistry) store.load(getClass());
                if (loaded != null)
                {
                    // renew leases for all the loaded services,
                    for (var service : services())
                    {
                        addOrUpdate(service);
                    }

                    // and copy the fields from it into this object.
                    copy(loaded);
                    trace("Loaded registry");
                }
                else
                {
                    trace("Unable to load registry");
                }
            }
            catch (Exception e)
            {
                warning(e, "Unable to load service registry");
            }
        });

        return this;
    }

    @Override
    public synchronized boolean start()
    {
        if (!running)
        {
            running = true;

            // Save the service registry on shutdown
            KivaKitShutdownHook.register(FIRST, () -> store.save(this));

            // and also every 30 seconds, in case we go down.
            KivaKitThread.repeat(this, "ServiceRegistrySaver", Duration.seconds(30).asFrequency(), () -> store.save(this));

            // Then start a thread to expire services and ports
            KivaKitThread.repeat(this, "ServiceRegistryReaper", Duration.seconds(30).asFrequency(), () ->
                    lock.write(() ->
                    {
                        // Go through each service
                        trace("Checking service expiration times");
                        for (var service : new HashSet<>(discoverServices().get()))
                        {
                            // and if it has not been renewed recently enough,
                            if (isExpired(service))
                            {
                                // then expire it
                                trace("Expiring $", service);
                                expire(service);
                            }
                        }

                        // then go through reserved ports
                        trace("Looking for expired ports");
                        for (var port : new HashSet<>(reservedPortToExpirationTime.keySet()))
                        {
                            // and if the port expired too long ago then the expired service is probably
                            // not ever coming back online to reclaim the port it registered
                            var expiredAt = reservedPortToExpirationTime.get(port);
                            if (expiredAt.elapsedSince().isGreaterThan(settings().portReservationExpirationTime()))
                            {
                                // so we cancel the port reservation so the port can be re-used
                                trace("Expired port $", port);
                                reservedPortToExpirationTime.remove(port);
                            }
                        }
                    }));
        }

        return true;
    }

    public BaseServiceRegistry updater(ServiceRegistryUpdater updater)
    {
        this.updater = updater;
        return this;
    }

    @UmlExcludeMember
    protected boolean isPortAvailable(int portNumber)
    {
        return !registeredPorts.contains(portNumber) && reservedPortToExpirationTime.get(portNumber) == null;
    }

    @UmlExcludeMember
    protected boolean isRegistered(Service service)
    {
        return portToService.containsValue(service);
    }

    @UmlExcludeMember
    protected ReadWriteLock lock()
    {
        return lock;
    }

    private void copy(BaseServiceRegistry that)
    {
        lock.write(() ->
        {
            portToService = that.portToService;
            serviceTypeToServices = that.serviceTypeToServices;
            applicationToServices = that.applicationToServices;
            renewedAt = that.renewedAt;
            registeredPorts = that.registeredPorts;
            reservedPortToExpirationTime = that.reservedPortToExpirationTime;
        });
    }

    private void expire(Service service)
    {
        lock.write(() ->
        {
            // Remove the service from registry indexes,
            information("Removing service $", service);
            var port = service.port();
            portToService.remove(port);
            serviceTypeToServices.removeFromSet(service.type(), service);
            applicationToServices.removeFromSet(service.application(), service);

            // remove it from the set of registered ports,
            registeredPorts.remove(port.number());

            // then add it to the map of reserved ports. If the expired service comes back online before
            // PORT_RESERVATION_TIME expires, it will still be able to reclaim its port. After then, the
            // port will be re-used.
            reservedPortToExpirationTime.put(port.number(), Time.now());
            information("Removed service $", service);
        });
    }

    private boolean isExpired(Service service)
    {
        // NOTE: read/write lock is already held by caller

        var elapsed = renewedAt.getOrDefault(service, Time.now()).elapsedSince();
        trace("Service $ was last renewed $ ago", service, elapsed);
        return elapsed.isGreaterThan(settings().serviceRegistrationExpirationTime());
    }

    private Result<Set<Service>> result(Collection<Service> set)
    {
        if (set != null)
        {
            var bound = Set.copyOf(set)
                    .stream()
                    .filter(Service::isBound)
                    .collect(Collectors.toSet());

            return Result.result(bound);
        }

        return result(Sets.empty());
    }

    @NotNull
    private Collection<Service> services()
    {
        return portToService.values();
    }
}

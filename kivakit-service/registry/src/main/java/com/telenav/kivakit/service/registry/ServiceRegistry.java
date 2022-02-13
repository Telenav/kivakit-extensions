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

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRegistry;
import com.telenav.kivakit.service.registry.registries.BaseServiceRegistry;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * For details on what service registries are and how they work, refer to ServiceRegistryClient. The client is a REST +
 * JSON proxy to {@link LocalServiceRegistry} and {@link NetworkServiceRegistry} processes which provides a public API
 * to the key methods in this class. Further details on service registry implementation are also available in {@link
 * BaseServiceRegistry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseServiceRegistry
 * @see LocalServiceRegistry
 * @see Service
 * @see ServiceType
 * @see Application.Identifier
 * @see Port
 * @see Result
 */
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlNote(text = "Use ServiceRegistryClient to register and discover services")
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public interface ServiceRegistry extends ComponentMixin
{
    /**
     * <b>Not public API</b>
     * <p>
     * Adds or updates registration information and renews the lease for the given service.
     */
    @NotNull
    Result<Boolean> addOrUpdate(Service service);

    /**
     * @return All applications that have registered a service
     */
    @NotNull
    Result<Set<Application.Identifier>> discoverApplications(Scope scope);

    /**
     * @return All the hosts that have registered services
     */
    default Result<Set<Host>> discoverHosts()
    {
        var services = discoverServices();
        if (services.succeeded())
        {
            var hosts = new HashSet<Host>();
            services.get().forEach(service -> hosts.add(service.port().host()));
            return Result.succeeded(hosts);
        }
        return Result.failed("Unable to find hosts");
    }

    /**
     * @return Any service running on the given port. Since a {@link Port} includes the host it is unique and only a
     * single service is returned since only one service can be running on a specific port on a specific host.
     */
    @NotNull
    Result<Service> discoverPortService(Port port);

    /**
     * Any application services of the given type
     */
    @NotNull
    Result<Set<Service>> discoverServices(Application.Identifier application, ServiceType type);

    /**
     * All services registered by the given application
     */
    @NotNull
    Result<Set<Service>> discoverServices(Application.Identifier application);

    /**
     * @return All services registered with this registry
     */
    @NotNull
    Result<Set<Service>> discoverServices();

    /**
     * All services of the given type that have been registered with this registry
     */
    @NotNull
    Result<Set<Service>> discoverServices(ServiceType type);

    /**
     * @return True if this is a {@link LocalServiceRegistry}
     */
    default boolean isLocal()
    {
        return this instanceof LocalServiceRegistry;
    }

    /**
     * @return True if this is a {@link NetworkServiceRegistry}
     */
    default boolean isNetwork()
    {
        return this instanceof NetworkServiceRegistry;
    }

    /**
     * Registers a {@link Service}, returning a {@link Service} object that has been bound to a unique physical port (on
     * a specific host) and which has a lease on that port for a few minutes. The resulting new registration information
     * will be immediately passed on to the {@link NetworkServiceRegistry}.
     * <p>
     * This method is only implemented by {@link LocalServiceRegistry} since the {@link NetworkServiceRegistry} is
     * updated through information propagated from local registries.
     * </p>
     *
     * @param service The service to register
     * @return The registered service, bound to a port
     */
    default @NotNull
    Result<Service> register(Service service)
    {
        return unsupported();
    }

    /**
     * Renews the lease on a service that has been registered with {@link #register(Service)} on the local host. The
     * resulting renewal information will be passed on to the {@link NetworkServiceRegistry}.
     * <p>
     * This method is only implemented by {@link LocalServiceRegistry} since the {@link NetworkServiceRegistry} is
     * updated through information propagated from local registries.
     * </p>
     */
    default @NotNull
    Result<Service> renew(Service service)
    {
        return unsupported();
    }

    default ServiceRegistrySettings settings()
    {
        return require(ServiceRegistrySettings.class);
    }
}

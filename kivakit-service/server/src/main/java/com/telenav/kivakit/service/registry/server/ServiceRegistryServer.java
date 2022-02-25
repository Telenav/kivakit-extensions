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

package com.telenav.kivakit.service.registry.server;

import com.telenav.kivakit.application.Server;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.ServiceRegistry;
import com.telenav.kivakit.service.registry.ServiceRegistryProject;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.registries.LocalServiceRegistry;
import com.telenav.kivakit.service.registry.registries.NetworkServiceRegistry;
import com.telenav.kivakit.service.registry.server.project.lexakai.diagrams.DiagramServer;
import com.telenav.kivakit.service.registry.server.rest.ServiceRegistryRestApplication;
import com.telenav.kivakit.service.registry.server.webapp.ServiceRegistryWebApplication;
import com.telenav.kivakit.service.registry.store.ServiceRegistryStore;
import com.telenav.kivakit.web.jersey.JerseyJettyPlugin;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.swagger.SwaggerJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerOpenApiJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebAppJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebJarJettyPlugin;
import com.telenav.kivakit.web.wicket.WicketJettyPlugin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Service registry server, including Wicket, REST and Swagger resources. Accepts these switches from the command line:
 *
 * <ul>
 *     <li>-scope=[network|localhost] - The scope of this registry, defaults to localhost</li>
 *     <li>-port=[number] - The port to run this server on</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramServer.class)
@UmlRelation(label = "persists to", referent = ServiceRegistryStore.class)
@UmlRelation(label = "creates", referent = ServiceRegistry.class)
@UmlRelation(label = "updates", referent = ServiceRegistry.class)
@UmlRelation(label = "searches", referent = ServiceRegistry.class)
@UmlNote(text = "For REST API details, refer to Swagger documentation provided by this server")
@LexakaiJavadoc(complete = true)
public class ServiceRegistryServer extends Server
{
    private static final Lazy<ServiceRegistryServer> project = Lazy.of(ServiceRegistryServer::new);

    public static ServiceRegistryServer get()
    {
        return project.get();
    }

    public static void main(String[] arguments)
    {
        get().run(arguments);
    }

    private final SwitchParser<Integer> PORT = SwitchParser
            .integerSwitchParser(this, "first-port", "The first port in the range of ports to be allocated")
            .defaultValue(50_000)
            .optional()
            .build();

    private final SwitchParser<Scope.Type> SCOPE = SwitchParser
            .enumSwitchParser(this, "scope", "The scope of operation for this server", Scope.Type.class)
            .defaultValue(Scope.localhost().type())
            .optional()
            .build();

    private transient final Lazy<ServiceRegistry> serviceRegistry = Lazy.of(() ->
    {
        var registry = listenTo(get(SCOPE) == Scope.Type.NETWORK
                ? new NetworkServiceRegistry()
                : new LocalServiceRegistry(get(PORT)));

        registry.load();
        registry.start();

        return registry;
    });

    protected ServiceRegistryServer()
    {
        super(ServiceRegistryProject.get());
    }

    public boolean isLocal()
    {
        return !isNetwork();
    }

    public boolean isNetwork()
    {
        return scope() == Scope.Type.NETWORK;
    }

    public int port()
    {
        return commandLine().get(PORT);
    }

    public Scope.Type scope()
    {
        return commandLine().get(SCOPE);
    }

    public ServiceRegistry serviceRegistry()
    {
        return serviceRegistry.get();
    }

    @Override
    protected void onRun()
    {
        showCommandLine();

        // Determine what port to use for the server,
        var settings = require(ServiceRegistrySettings.class);
        var port = isNetwork()
                ? settings.networkServiceRegistryPort().number()
                : settings.localServiceRegistryPort();

        // create the Jersey REST application,
        var application = listenTo(new ServiceRegistryRestApplication());

        // and start up Jetty with Swagger, Jersey and Wicket.
        listenTo(new JettyServer("/"))
                .port(port)
                .mount("/*", new WicketJettyPlugin(ServiceRegistryWebApplication.class))
                .mount("/open-api/*", new SwaggerOpenApiJettyPlugin(application))
                .mount("/docs/*", new SwaggerJettyPlugin(openApiAssetsFolder(), port))
                .mount("/webapp/*", new SwaggerWebAppJettyPlugin())
                .mount("/webjar/*", new SwaggerWebJarJettyPlugin())
                .mount("/*", new JerseyJettyPlugin(application))
                .start();
    }

    /**
     * @return The resource folder containing static assets for reference by OpenAPI .yaml files and KivaKit OpenApi
     * annotations. For example, a microservice might want to include an OAS .yaml file. If this method is not
     * overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder openApiAssetsFolder()
    {
        var type = ensureNotNull(Type.forName("com.telenav.kivakit.web.swagger.SwaggerJettyPlugin"));
        return Package.packageFrom(this, type.type(), "assets/openapi");
    }

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.objectSet(PORT, SCOPE);
    }
}

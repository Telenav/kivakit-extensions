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

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.web.jetty.resources.BaseAssetsJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseFilterJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseServletJettyPlugin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.log.StdErrLog;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * A convenient way to use Jetty for simple web applications.
 * <p>
 * A Jetty web server can quickly and easily be started on a particular port with a given set of servlets and filters.
 * </p>
 * <p>
 * Support is available for:
 * </p>
 * <ul>
 *     <li>Wicket</li>
 *     <li>Jersey</li>
 *     <li>Swagger</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 *
 * <pre>
 * // Create the Jersey REST application with Swagger OpenAPI and documentation,
 * var application = new ServiceRegistryRestApplication();
 * var jersey = new JettyJersey(application);
 * var swaggerOpenApi = new JettySwaggerOpenApi(swaggerConfiguration(application));
 * var swaggerWebJar = new JettySwaggerWebJar(application);
 *
 * // create the Wicket WebApplication,
 * var wicket = new JettyWicket(ServiceRegistryWebApplication.class);
 *
 * // and start up Jetty.
 * listenTo(new JettyServer())
 *     .port(port)
 *     .add("/*", wicket)
 *     .add("/api/*", jersey)
 *     .add("/open-api/*", swaggerOpenApi)
 *     .add("/docs/*", new JettySwaggerDocs(port))
 *     .add("/webjar/*", swaggerWebJar)
 *     .start();
 *
 * JettySwaggerConfiguration swagger(ServiceRegistryRestApplication application)
 * {
 *     var api = new OpenAPI().info(new Info()
 *         .title("KivaKit Service Registry")
 *         .description("Registry of KivaKit services.")
 *         .contact(new Contact().email("jonathanl@telenav.com"))
 *         .license(new License().name("Copyright 2011-2021 Telenav, Inc.")));
 *
 *     var resources = Set.of(application.getClass());
 *     return new JettySwaggerConfiguration(api, application, resources);
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @LexakaiJavadoc(complete = true)
public class JettyServer extends BaseComponent implements
        Startable,
        Stoppable<Duration>
{
    public static void configureLogging()
    {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        org.eclipse.jetty.util.log.Log.setLog(new StdErrLog());
    }

    /** The static assets to install when Jetty starts */
    private final List<BaseAssetsJettyPlugin> assets = new ArrayList<>();

    /** The filters to install when Jetty starts */
    private final List<BaseFilterJettyPlugin> filters = new ArrayList<>();

    /** Any CORS filter */
    private FilterHolder crossOriginFilter;

    /** The port to run Jetty on */
    private int port;

    /**
     * The root path relative to this server. By default, this is the name of the microservice, like
     * <i>/my-microservice</i>
     */
    private final String root;

    /** Jetty server instance */
    private Server server;

    /** The servlets to install when Jetty starts */
    private final List<BaseServletJettyPlugin> servlets = new ArrayList<>();

    public JettyServer(String root)
    {
        this.root = root;

        configureLogging();
    }

    /**
     * Adds a cross-origin filter to this server for all requests
     *
     * @param allowedMethods The {@link HttpMethod}s allowed
     * @param allowedOrigins The origins that a request can come from
     * @param allowedHeaders The headers that are allowed
     * @param maximumPreflightAge The maximum age of pre-flight requests
     */
    public void addCrossOriginFilter(ObjectSet<HttpMethod> allowedMethods,
                                     ObjectSet<String> allowedOrigins,
                                     ObjectSet<String> allowedHeaders,
                                     Duration maximumPreflightAge)
    {
        if (crossOriginFilter != null)
        {
            fail("Cannot add more than one cross-origin filter at this time");
        }

        // Add cross-origin filter
        crossOriginFilter = new FilterHolder();
        crossOriginFilter.setInitParameter("allowedOrigins", allowedOrigins.join(","));
        crossOriginFilter.setInitParameter("allowedMethods", allowedMethods.join(","));
        crossOriginFilter.setInitParameter("allowedHeaders", allowedHeaders.join(","));
        crossOriginFilter.setInitParameter("preflightMaxAge", "" + (int) maximumPreflightAge.asSeconds());
        crossOriginFilter.setInitParameter("allowCredentials", "true");
        crossOriginFilter.setFilter(new CrossOriginFilter());
    }

    @Override
    public boolean isRunning()
    {
        return server != null;
    }

    @Override
    public Duration maximumStopTime()
    {
        return Duration.MAXIMUM;
    }

    public JettyServer mount(String path, BaseFilterJettyPlugin filter)
    {
        filter.path(resolvePath(path));
        filters.add(filter);
        return this;
    }

    public JettyServer mount(String path, BaseServletJettyPlugin servlet)
    {
        servlet.path(resolvePath(path));
        servlets.add(servlet);
        return this;
    }

    public JettyServer mount(String path, BaseAssetsJettyPlugin assets)
    {
        assets.path(resolvePath(path));
        this.assets.add(assets);
        return this;
    }

    public JettyServer port(int port)
    {
        ensure(port > 0);
        this.port = port;
        return this;
    }

    public String resolvePath(String path)
    {
        return Paths.concatenate(root, path);
    }

    public boolean start()
    {
        try
        {
            // Create and start Jetty
            server().start();
            narrate("Jetty started on port ${integer}", port);
            return true;
        }
        catch (Exception e)
        {
            throw problem(e, "Couldn't start embedded Jetty web server").asException();
        }
    }

    @Override
    public void stop(Duration wait)
    {
        if (isRunning())
        {
            try
            {
                server.stop();
            }
            catch (Exception e)
            {
                warning("Unable to stop Jetty server");
            }
        }
    }

    public void waitForTermination()
    {
        try
        {
            server.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private ServerConnector httpConnector(Server server)
    {
        // Return an HTTP Jetty server connector for the port that was specified
        ServerConnector http = new ServerConnector(server);
        http.setPort(port);
        http.setIdleTimeout(Duration.hours(1).milliseconds());
        return http;
    }

    private Server server()
    {
        if (server == null)
        {
            // Create Jetty server and add HTTP connector to it,
            server = new Server();
            server.addConnector(httpConnector(server));
            server.setStopAtShutdown(true);

            // create a "ServletContextHandler", which is a really confusing name that really means
            // something like "the place where you can register all kinds of stuff that the server
            // will use when handling requests, including but not limited to servlets"
            var servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
            servletContext.setContextPath("/");
            servletContext.setServer(server);
            servletContext.setSessionHandler(new SessionHandler());
            servletContext.setResourceBase(".");

            // Add any cross-origin filter
            if (crossOriginFilter != null)
            {
                servletContext.addFilter(crossOriginFilter, "/*", EnumSet.of(DispatcherType.REQUEST));
            }

            // then for each JettyResource,
            assets.forEach(asset ->
            {
                // add it to the servlet context at the given path,
                servletContext.addServlet(asset.holder(), asset.path());
                narrate("Mounted assets $ => $", asset.path(), asset.name());
            });

            // and for each JettyFilter,
            filters.forEach(filter ->
            {
                // add it to the servlet context at the given path,
                servletContext.addFilter(filter.holder(), filter.path(), filter.dispatchers());
                narrate("Mounted filter $ => $", filter.path(), filter.name());
            });

            // and for each JettyServlet,
            servlets.forEach(servlet ->
            {
                // add it to the servlet context at the given path,
                servletContext.addServlet(servlet.holder(), servlet.path());
                narrate("Mounted servlet $ => $", servlet.path(), servlet.name());
            });

            // and finally, add the servlet context as the handler for server requests.
            server.setHandler(servletContext);
        }
        return server;
    }
}

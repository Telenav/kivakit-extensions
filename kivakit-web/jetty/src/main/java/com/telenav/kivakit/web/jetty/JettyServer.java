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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.web.jetty.resources.BaseAssetsJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseFilterJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseServletJettyPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.string.Paths.pathConcatenate;
import static com.telenav.kivakit.core.time.Duration.FOREVER;
import static com.telenav.kivakit.core.time.Duration.hours;
import static jakarta.servlet.DispatcherType.REQUEST;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

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
 * // Create the Jersey REST application with Swagger documentation,
 * var application = new MyRestApplication();
 * var jersey = new JerseyJettyPlugin(application);
 * var swaggerOpenApi = new SwaggerOpenApiJettyPlugin(application);
 * var swaggerWebJar = new SwaggerWebJarAssetJettyPlugin();
 *
 * // create the Wicket WebApplication,
 * var wicket = new WicketJettyPlugin(ServiceRegistryWebApplication.class);
 *
 * // and start up Jetty.
 * listenTo(new JettyServer())
 *     .port(port)
 *     .add("/*", wicket)
 *     .add("/api/*", jersey)
 *     .add("/open-api/*", swaggerOpenApi)
 *     .add("/docs/*", new SwaggerIndexJettyPlugin(assets(), port))
 *     .add("/webjar/*", swaggerWebJar)
 *     .start();
 * </pre>
 *
 * <p><b>Mounting Plugins</b></p>
 *
 * <ul>
 *     <li>{@link #mount(String, BaseAssetsJettyPlugin)}</li>
 *     <li>{@link #mount(String, BaseFilterJettyPlugin)}</li>
 *     <li>{@link #mount(String, BaseServletJettyPlugin)}</li>
 * </ul>
 *
 * <p><b>Configuration</b></p>
 *
 * <ul>
 *     <li>{@link #port(int)}</li>
 * </ul>
 *
 * <p><b>CORS</b></p>
 *
 * <ul>
 *     <li>{@link #addCrossOriginFilter(ObjectSet, ObjectSet, ObjectSet, Duration)}</li>
 * </ul>
 *
 * <p><b>Starting and Stopping</b></p>
 *
 * <ul>
 *     <li>{@link #isRunning()}</li>
 *     <li>{@link #maximumStopTime()}</li>
 *     <li>{@link #start()}</li>
 *     <li>{@link #stop(Duration)}</li>
 *     <li>{@link #waitForTermination()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class JettyServer extends BaseComponent implements
        Startable,
        Stoppable<Duration>
{
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

    /**
     * @param root The root path relative to this server
     */
    public JettyServer(String root)
    {
        this.root = root;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning()
    {
        return server != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration maximumStopTime()
    {
        return FOREVER;
    }

    /**
     * Mounts the given filter plugin on this Jetty server
     *
     * @param path The path to mount on
     * @param filter The filter to mount
     * @return This object, for method chaining
     */
    public JettyServer mount(String path, BaseFilterJettyPlugin filter)
    {
        filter.path(resolvePath(path));
        filters.add(filter);
        return this;
    }

    /**
     * Mounts the given servlet plugin on this Jetty server
     *
     * @param path The path to mount on
     * @param servlet The servlet to mount
     * @return This object, for method chaining
     */
    public JettyServer mount(String path, BaseServletJettyPlugin servlet)
    {
        servlet.path(resolvePath(path));
        servlets.add(servlet);
        return this;
    }

    /**
     * Mounts the given assets plugin on this Jetty server
     *
     * @param path The path to mount on
     * @param assets The assets to mount
     * @return This object, for method chaining
     */
    public JettyServer mount(String path, BaseAssetsJettyPlugin assets)
    {
        assets.path(resolvePath(path));
        this.assets.add(assets);
        return this;
    }

    /**
     * Sets the server port for Jetty to use
     *
     * @param port The port
     * @return This object, for method chaining
     */
    public JettyServer port(int port)
    {
        ensure(port > 0);
        this.port = port;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Waits forever for this server to terminate
     */
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
        http.setIdleTimeout(hours(1).milliseconds());
        return http;
    }

    /**
     * Returns the given path, prefixed by the root path
     *
     * @param path The path
     * @return The path from the root
     */
    private String resolvePath(String path)
    {
        return pathConcatenate(root, path);
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
            var servletContext = new ServletContextHandler(SESSIONS);
            servletContext.setContextPath("/");
            servletContext.setServer(server);
            servletContext.setSessionHandler(new SessionHandler());
            servletContext.setResourceBase(".");

            // Add any cross-origin filter
            if (crossOriginFilter != null)
            {
                servletContext.addFilter(crossOriginFilter, "/*", EnumSet.of(REQUEST));
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

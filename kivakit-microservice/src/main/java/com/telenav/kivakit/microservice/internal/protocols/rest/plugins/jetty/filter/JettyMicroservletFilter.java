package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblemReportingTrait;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>Not public API</b>
 *
 * <p>
 * A Java Servlet API filter that handles {@link Microservlet} REST requests.
 * </p>
 *
 * <p><b>Microservlet Mounts</b></p>
 *
 * <p>
 * {@link Microservlet}s are mounted on paths with {@link #mount(MicroservletRestPath, Microservlet)}. When {@link
 * #doFilter(ServletRequest, ServletResponse, FilterChain)} is called by the servlet implementation, the path is parsed
 * and the associated microservlet (if any) is invoked.
 * </p>
 *
 * <p>
 * All HTTP methods result in a call to {@link Microservlet#onRespond(MicroservletRequest)}, where the parameter is the
 * posted JSON object (in the case of POST requests). The object returned from the onRequest method is written to the
 * response output in JSON format. The {@link Gson} object used to serialize and deserialize JSON objects is obtained
 * from the {@link MicroserviceRestService} passed to the filter constructor.
 * </p>
 *
 * <p><b>JAR Mounts</b></p>
 *
 * <p>
 * To permit robust backwards compatibility, JAR files can be mounted with {@link #mount(MountedApi)} When {@link
 * #doFilter(ServletRequest, ServletResponse, FilterChain)} is called by the servlet implementation, the path is parsed.
 * If there is a JAR mounted on the path, it is executed in a child process (if it is not already running) using the
 * given port for HTTP. The request is delegated to the child process' port.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletFilter extends BaseComponent implements
        Filter,
        HttpProblemReportingTrait
{
    /** Map from path to mounted JAR */
    private final Map<MicroservletRestPath, MountedApi> mountedApis = new HashMap<>();

    /** Map from path to mounted microservlet */
    @UmlAggregation(referent = Microservlet.class, label = "mounts on paths", referentCardinality = "many")
    private final Map<MicroservletRestPath, MountedMicroservlet> mountedMicroservlets = new HashMap<>();

    /** The microservice rest application */
    @UmlAggregation
    private final MicroserviceRestService service;

    /**
     * @param service The REST application that is using this filter
     */
    public JettyMicroservletFilter(MicroserviceRestService service)
    {
        this.service = service;

        register(this);
    }

    @Override
    public void destroy()
    {
    }

    /**
     * Main Java Servlet API filter entrypoint
     *
     * @param servletRequest The servlet request
     * @param servletResponse The servlet response
     * @param filterChain The filter chain to delegate to
     */
    @Override
    @UmlRelation(label = "creates", referent = JettyMicroservletRequestCycle.class)
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
    {
        try
        {
            // Cast request and response to HTTP subclasses,
            var httpRequest = (HttpServletRequest) servletRequest;
            var httpResponse = (HttpServletResponse) servletResponse;

            // parse the HTTP method,
            var handled = false;
            var method = HttpMethod.parse(httpRequest.getMethod());
            if (method != null)
            {
                // create microservlet request cycle,
                var cycle = listenTo(new JettyMicroservletRequestCycle(service, httpRequest, httpResponse));

                // attach it to the current thread,
                JettyMicroservletRequestCycle.attach(cycle);

                // resolve any microservlet at the requested path,
                var mounted = resolveMicroservlet(new MicroservletRestPath(cycle.request().path(), method));
                if (mounted != null)
                {
                    // and handle the request.
                    tryCatch(() -> mounted.handleRequest(method, cycle),
                            "REST $ method to $ failed", method.name(), mounted.microservlet.name());
                    handled = true;
                }
                else
                {
                    // and if no microservlet was resolved, try resolving the path to a JAR mount,
                    var mountedJar = resolveApi(new MicroservletRestPath(cycle.request().path(), HttpMethod.NONE));
                    if (mountedJar != null)
                    {
                        // and handle the request that way.
                        tryCatch(() -> mountedJar.handleRequest(method, cycle),
                                "REST $ method to $ failed", method.name(), mountedJar);
                        handled = true;
                    }
                }
            }
            else
            {
                problem("Invalid request method");
            }

            if (!handled)
            {
                try
                {
                    // If the request wasn't handled, pass it down the filter chain.
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                catch (Exception e)
                {
                    problem(e, "Exception thrown by filter chain");
                }
            }
        }
        catch (Exception e)
        {
            problem(e, "Servlet doFilter() method failed");
        }
        finally
        {
            JettyMicroservletRequestCycle.detach();
        }
    }

    @Override
    public void init(FilterConfig filterConfig)
    {
    }

    /**
     * @param path The request path
     * @return Any microservice at the given path with the given request method, or null if none exists
     */
    public MountedMicroservlet microservlet(MicroservletRestPath path)
    {
        return mountedMicroservlets.get(path);
    }

    /**
     * @return The set of all microservlet paths. This includes an automatically appended HTTP method name.
     */
    public Set<MicroservletRestPath> microservletPaths()
    {
        return mountedMicroservlets.keySet();
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(MicroservletRestPath path, Microservlet<?, ?> microservlet)
    {
        var microservice = service.microservice();
        var existing = mountedMicroservlets.get(path);
        if (existing != null)
        {
            problem("$: There is already a $ microservlet mounted on $: ${class}",
                    microservice.name(), path.method(), path.path(), existing.getClass()).throwAsIllegalStateException();
        }

        var mounted = listenTo(new MountedMicroservlet(service));
        mounted.microservlet = microservlet;
        mounted.path = path;

        mountedMicroservlets.put(path, mounted);
        information("Mounted microservlet $ $ => $", path.method().name(), path, microservlet.name());
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(MountedApi api)
    {
        // If there is already an existing api for the given path,
        var existing = mountedApis.get(api.path());
        if (existing != null)
        {
            // then throw an exception,
            problem("There is already an API mounted on $: $",
                    api.path(), existing).throwAsIllegalStateException();
        }

        // otherwise, launch the API,
        if (!api.maybeLaunch())
        {
            // or fail trying,
            problem("Unable to launch API JAR: $", api).throwAsIllegalStateException();
        }

        // and finally, put our API in the map of mounted APIs.
        mountedApis.put(api.path(), api);
        information("Mounted $", api);
    }

    /**
     * Returns the collection of all {@link MountedApi}s  for this filter.
     */
    public Collection<MountedApi> mountedApis()
    {
        return mountedApis.values();
    }

    /**
     * Resolves the given rest path to a JAR. Paths are resolved to the most specific match by removing the last
     * component of the path until a mounted JAR is found
     *
     * @param path The mount path
     * @return The JAR at the given path, or null if the path does not map to any JAR
     */
    private MountedApi resolveApi(final MicroservletRestPath path)
    {
        int removed = 0;
        for (var at = path; at.isNonEmpty(); at = at.withoutLast(), removed++)
        {
            var mounted = mountedApis.get(at);
            if (mounted != null)
            {
                return mounted;
            }
        }

        return null;
    }

    /**
     * Resolves the given file path to a microservice. Paths are resolved to the most specific match by removing the
     * last component of the path until a microservlet is found.
     *
     * @param path The mount path
     * @return The microservlet at the given path, or null if the path does not map to any microservlet
     */
    private MountedMicroservlet resolveMicroservlet(MicroservletRestPath path)
    {
        int removed = 0;
        for (var at = path; at.isNonEmpty(); at = at.withoutLast(), removed++)
        {
            var mounted = microservlet(at);
            if (mounted != null)
            {
                mounted.parameters = new MicroservletRestPath(path.path().last(removed), path.method());
                return mounted;
            }
        }

        return null;
    }
}

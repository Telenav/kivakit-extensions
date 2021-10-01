package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter;

import com.google.gson.Gson;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.language.time.PreciseDuration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.internal.cycle.ProblemReportingTrait;
import com.telenav.kivakit.microservice.rest.microservlet.internal.metrics.MetricReportingTrait;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.MicroservletPath;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.JettyMicroservletRequestCycle;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest.HttpMethod.GET;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

/**
 * <b>Not public API</b>
 *
 * <p>
 * A Java Servlet API filter that handles {@link Microservlet} requests.
 * </p>
 *
 * <p>
 * {@link Microservlet}s are mounted on paths with {@link #mount(String, Microservlet)}. When {@link
 * #doFilter(ServletRequest, ServletResponse, FilterChain)} is called by the servlet implementation, the path is parsed
 * and the associated microservlet (if any) is invoked.
 * </p>
 *
 * <p>
 * The POST method results in a call to {@link Microservlet#onPost(MicroservletRequest)}, where the parameter is the
 * posted JSON object. The GET method results in a call to {@link Microservlet#onGet(MicroservletRequest)}. The object
 * returned from either method is written to the response output in JSON format. The {@link Gson} object used to
 * serialize and deserialize JSON objects is obtained from the {@link MicroserviceRestApplication} passed to the filter
 * constructor.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletFilter implements Filter, ComponentMixin, ProblemReportingTrait, MetricReportingTrait
{
    private static class ResolvedMicroservlet
    {
        /** The microservlet */
        Microservlet<?, ?> microservlet;

        /** The path to the microservlet */
        MicroservletPath path;

        /** Any path parameters */
        MicroservletPath parameters;
    }

    /** Map from path to microservlet */
    @UmlAggregation(referent = Microservlet.class, label = "mounts on paths", referentCardinality = "many")
    private final Map<MicroservletPath, Microservlet<?, ?>> pathToMicroservlet = new HashMap<>();

    /** The microservice rest application */
    @UmlAggregation
    private final MicroserviceRestApplication restApplication;

    /** The name of this object for debugging purposes */
    private String objectName;

    /**
     * @param restApplication The REST application that is using this filter
     */
    public JettyMicroservletFilter(final MicroserviceRestApplication restApplication)
    {
        this.restApplication = restApplication;

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
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
    {
        // Cast request and response to HTTP subclasses,
        final var httpRequest = (HttpServletRequest) servletRequest;
        final var httpResponse = (HttpServletResponse) servletResponse;
        final var method = MicroservletRequest.HttpMethod.valueOf(httpRequest.getMethod());

        // create microservlet request cycle,
        final var cycle = listenTo(new JettyMicroservletRequestCycle(restApplication, httpRequest, httpResponse));

        // attach it to the current thread,
        JettyMicroservletRequestCycle.attach(cycle);

        try
        {
            // resolve the microservlet at the requested path,
            final var resolved = resolve(new MicroservletPath(cycle.request().path(), method));
            if (resolved != null)
            {
                try
                {
                    // record the start time,
                    final var start = Time.now();
                    final var startCpuTime = PreciseDuration.cpuTime();

                    // handle the request,
                    handleRequest(method, cycle, resolved);

                    // and report how long the request took.
                    metric("request-wall-clock-time", start.elapsedSince());
                    metric("request-cpu-time", PreciseDuration.cpuTime().minus(startCpuTime));
                }
                catch (final Exception e)
                {
                    problem(SC_INTERNAL_SERVER_ERROR, e, "REST $ method to $ failed", method.name(), resolved.microservlet.name());
                }
            }
            else
            {
                try
                {
                    // If there is no microservlet, pass the request down the filter chain.
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                catch (final Exception e)
                {
                    problem(SC_INTERNAL_SERVER_ERROR, e, "Exception thrown by filter chain");
                }
            }
        }
        finally
        {
            cycle.reportMetrics();

            JettyMicroservletRequestCycle.detach();
        }
    }

    @Override
    public void init(final FilterConfig filterConfig)
    {
    }

    /**
     * @param path The request path
     * @return Any microservice at the given path with the given request method, or null if none exists
     */
    public Microservlet<?, ?> microservlet(final MicroservletPath path)
    {
        return pathToMicroservlet.get(path);
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(final String path, final Microservlet<?, ?> microservlet)
    {
        final var microservice = restApplication().microservice();
        for (final var method : microservlet.supportedMethods())
        {
            var servletPath = new MicroservletPath(path, method);
            final var existing = pathToMicroservlet.get(servletPath);
            if (existing != null)
            {
                problem("There is already a $ microservlet mounted on $: ${class}",
                        method, servletPath, existing.getClass()).throwAsIllegalStateException();
            }
            pathToMicroservlet.put(servletPath, microservlet);
            information("Mounted $ microservlet $ => $", method.name(), servletPath, microservlet.name());
        }
    }

    @Override
    public void objectName(final String objectName)
    {
        this.objectName = objectName;
    }

    @Override
    public String objectName()
    {
        return objectName;
    }

    /**
     * @return The set of all microservice paths. This includes an automatically appended HTTP method name.
     */
    public Set<MicroservletPath> paths()
    {
        return pathToMicroservlet.keySet();
    }

    /**
     * @return The REST application that installed this filter
     */
    public MicroserviceRestApplication restApplication()
    {
        return restApplication;
    }

    /**
     * Handles GET and POST methods to the given microservlet.
     *
     * @param method The HTTP request method
     * @param cycle The request cycle
     * @param resolved The microservlet to call
     */
    private void handleRequest(final MicroservletRequest.HttpMethod method,
                               final JettyMicroservletRequestCycle cycle,
                               final ResolvedMicroservlet resolved)
    {
        // Get the response object, microservlet, path and parameters,
        final var response = cycle.response();
        final var microservlet = resolved.microservlet;
        final var parameters = cycle.request().parameters(resolved.parameters.path());

        // and if the request method is
        switch (method)
        {
            case POST:
            {
                // then convert the posted JSON to an object of the request type,
                final MicroservletRequest request;
                if (!parameters.isEmpty())
                {
                    // converting any parameters to a JSON request object,
                    request = cycle.gson().fromJson(parameters.asJson(), microservlet.requestType());
                }
                else
                {
                    // or if there are no parameters, converting the posted entity.
                    request = listenTo(cycle.request().readObject(microservlet.requestType()));
                }

                // Then, we respond with the object returned from onPost().
                if (request != null)
                {
                    listenTo(request);
                    response.writeObject(microservlet.post(request));
                }
            }
            break;

            case GET:
            case DELETE:
            {
                // then turn parameters into a JSON object and then treat that like it was POSTed.
                final var request = cycle.gson().fromJson(parameters.asJson(), microservlet.requestType());

                // Respond with the object returned from onGet.
                if (request != null)
                {
                    listenTo(request);
                    response.writeObject(method == GET ? microservlet.get(request) : microservlet.delete(request));
                }
            }
            break;

            default:
                problem(SC_METHOD_NOT_ALLOWED, "Method $ not supported", method.name());
                break;
        }
    }

    /**
     * Resolves the given file path to a microservice. Paths are resolved to the most specific match by removing the
     * last component of the path until a microservlet is found.
     *
     * @param path The mount path
     * @return The microservlet at the given path, or null if the path does not map to any microservlet
     */
    private ResolvedMicroservlet resolve(MicroservletPath path)
    {
        int removed = 0;
        for (var at = path; at.isNonEmpty(); at = at.withoutLast(), removed++)
        {
            final var microservlet = microservlet(at);
            if (microservlet != null)
            {
                var resolved = new ResolvedMicroservlet();
                resolved.microservlet = microservlet;
                resolved.path = path;
                resolved.parameters = new MicroservletPath(path.path().last(removed), path.method());
                return resolved;
            }
        }

        return null;
    }
}
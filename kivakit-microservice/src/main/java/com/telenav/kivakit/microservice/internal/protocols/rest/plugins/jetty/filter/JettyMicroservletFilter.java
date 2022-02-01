package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.google.gson.Gson;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.ProblemReportingTrait;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandlingStatistics;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

/**
 * <b>Not public API</b>
 *
 * <p>
 * A Java Servlet API filter that handles {@link Microservlet} REST requests.
 * </p>
 *
 * <p>
 * {@link Microservlet}s are mounted on paths with {@link #mount(MicroservletRestPath, Microservlet)}. When {@link
 * #doFilter(ServletRequest, ServletResponse, FilterChain)} is called by the servlet implementation, the path is parsed
 * and the associated microservlet (if any) is invoked.
 * </p>
 *
 * <p>
 * All HTTP methods result in a call to {@link Microservlet#onRequest(MicroservletRequest)}, where the parameter is the
 * posted JSON object (in the case of POST requests). The object returned from the onRequest method is written to the
 * response output in JSON format. The {@link Gson} object used to serialize and deserialize JSON objects is obtained
 * from the {@link MicroserviceRestService} passed to the filter constructor.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletFilter implements Filter, ComponentMixin, ProblemReportingTrait
{
    private static class ResolvedMicroservlet
    {
        /** The microservlet */
        Microservlet<?, ?> microservlet;

        /** The path to the microservlet */
        MicroservletRestPath path;

        /** Any path parameters */
        MicroservletRestPath parameters;
    }

    /** The name of this object for debugging purposes */
    private String objectName;

    /** Map from path to microservlet */
    @UmlAggregation(referent = Microservlet.class, label = "mounts on paths", referentCardinality = "many")
    private final Map<MicroservletRestPath, Microservlet<?, ?>> pathToMicroservlet = new HashMap<>();

    /** The microservice rest application */
    @UmlAggregation
    private final MicroserviceRestService restApplication;

    /**
     * @param restApplication The REST application that is using this filter
     */
    public JettyMicroservletFilter(MicroserviceRestService restApplication)
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
                var cycle = listenTo(new JettyMicroservletRequestCycle(restApplication, httpRequest, httpResponse));

                // attach it to the current thread,
                JettyMicroservletRequestCycle.attach(cycle);

                // resolve the microservlet at the requested path,
                var resolved = resolve(new MicroservletRestPath(cycle.request().path(), method));
                if (resolved != null)
                {
                    try
                    {
                        // and handle the request.
                        handleRequest(method, cycle, resolved);
                    }
                    catch (Exception e)
                    {
                        problem(e, "REST $ method to $ failed", method.name(), resolved.microservlet.name());
                    }
                    handled = true;
                }
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
    public Microservlet<?, ?> microservlet(MicroservletRestPath path)
    {
        return pathToMicroservlet.get(path);
    }

    /**
     * @return The list of all microservlets installed on this filter
     */
    public ObjectList<Microservlet<?, ?>> microservlets()
    {
        return ObjectList.objectList(pathToMicroservlet.values());
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(MicroservletRestPath path, Microservlet<?, ?> microservlet)
    {
        var microservice = restApplication().microservice();
        var existing = pathToMicroservlet.get(path);
        if (existing != null)
        {
            problem("There is already a $ microservlet mounted on $: ${class}",
                    path.method(), path.path(), existing.getClass()).throwAsIllegalStateException();
        }
        pathToMicroservlet.put(path, microservlet);
        information("Mounted $ microservlet $ => $", path.method().name(), path, microservlet.name());
    }

    @Override
    public void objectName(String objectName)
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
    public Set<MicroservletRestPath> paths()
    {
        return pathToMicroservlet.keySet();
    }

    /**
     * @return The REST application that installed this filter
     */
    public MicroserviceRestService restApplication()
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
    private void handleRequest(HttpMethod method,
                               JettyMicroservletRequestCycle cycle,
                               ResolvedMicroservlet resolved)
    {
        var statistics = new MicroservletRequestHandlingStatistics();
        statistics.start();
        statistics.path(resolved.path.key());

        try
        {

            // Get the response object, microservlet, path and parameters,
            var response = cycle.response();
            var microservlet = resolved.microservlet;
            var requestType = microservlet.requestType();
            var parameters = cycle.request().parameters(resolved.parameters.path());

            // and if the request method is
            switch (method)
            {
                case POST:
                {
                    // and there is a request body,
                    MicroservletRequest request;
                    if (cycle.request().hasBody())
                    {
                        // then convert the JSON in the body to a request object,
                        request = cycle.request().readObject(requestType);
                    }
                    else
                    {
                        // otherwise convert any parameters to a JSON request object,
                        request = cycle.gson().fromJson(parameters.asJson(), requestType);
                    }

                    // Then, we respond with the object returned from onPost().
                    if (request != null)
                    {
                        listenTo(request);
                        restApplication.onRequesting(request, method);
                        response.writeObject(microservlet.request(request));
                        restApplication.onRequested(request, method);
                    }
                    else
                    {
                        response.writeObject(null);
                    }
                }
                break;

                case GET:
                case DELETE:
                {
                    // then turn parameters into a JSON object and then treat that like it was POSTed.
                    var request = cycle.gson().fromJson(parameters.asJson(), requestType);

                    // Respond with the object returned from onGet.
                    if (request != null)
                    {
                        listenTo(request);
                        restApplication.onRequesting(request, method);
                        response.writeObject(microservlet.request(request));
                        restApplication.onRequested(request, method);
                    }
                    else
                    {
                        response.writeObject(null);
                    }
                }
                break;

                default:
                    problem(SC_METHOD_NOT_ALLOWED, "Method $ not supported", method.name());
                    break;
            }
        }
        finally
        {
            statistics.end();
            restApplication.onRequestStatistics(statistics);
        }
    }

    /**
     * Resolves the given file path to a microservice. Paths are resolved to the most specific match by removing the
     * last component of the path until a microservlet is found.
     *
     * @param path The mount path
     * @return The microservlet at the given path, or null if the path does not map to any microservlet
     */
    private ResolvedMicroservlet resolve(MicroservletRestPath path)
    {
        int removed = 0;
        for (var at = path; at.isNonEmpty(); at = at.withoutLast(), removed++)
        {
            var microservlet = microservlet(at);
            if (microservlet != null)
            {
                var resolved = new ResolvedMicroservlet();
                resolved.microservlet = microservlet;
                resolved.path = path;
                resolved.parameters = new MicroservletRestPath(path.path().last(removed), path.method());
                return resolved;
            }
        }

        return null;
    }
}

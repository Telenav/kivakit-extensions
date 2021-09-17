package com.telenav.kivakit.microservice.rest.microservlet.jetty;

import com.google.gson.Gson;
import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.cycle.MicroservletRequestCycle;
import com.telenav.kivakit.resource.path.FilePath;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
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
 * The POST method results in a call to {@link Microservlet#onPost(Object)}, where the parameter is the posted JSON
 * object. The GET method results in a call to {@link Microservlet#onGet()}. The object returned from either method is
 * written to the response output in JSON format. The {@link Gson} object used to serialize and deserialize JSON objects
 * is obtained from the {@link MicroserviceRestApplication} passed to the filter constructor.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class JettyMicroservletFilter implements Filter, ComponentMixin
{
    /** Map from path to servlet */
    private final Map<String, Microservlet> pathToServlet = new HashMap<>();

    /** The microservice rest application */
    private final MicroserviceRestApplication application;

    /**
     * @param application The REST application that is using this filter
     */
    public JettyMicroservletFilter(final MicroserviceRestApplication application)
    {
        this.application = application;
    }

    @Override
    public void destroy()
    {
    }

    /**
     * Main Servlet API filter entrypoint
     *
     * @param servletRequest The servlet request
     * @param servletResponse The servlet response
     * @param filterChain The filter chain to delegate to
     */
    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
    {
        // Cast request and response to HTTP subclasses,
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;

        // create microservlet request cycle,
        var cycle = listenTo(new MicroservletRequestCycle(application, httpRequest, httpResponse));

        // resolve the microservlet at the requested path,
        final var servlet = resolve(cycle.request().path());
        if (servlet != null)
        {
            try
            {
                // and attach the request cycle to the (stateless) servlet via thread-local map.
                handleRequest(httpRequest.getMethod(), cycle, servlet);
            }
            catch (Exception e)
            {
                problem(e, "REST $ method to $ failed", httpRequest.getMethod(), servlet.objectName());
            }
        }
        else
        {
            try
            {
                // If there is no microservlet, pass the request down the filter chain.
                filterChain.doFilter(servletRequest, servletResponse);
            }
            catch (Exception e)
            {
                warning(e, "Exception thrown by filter chain");
            }
        }
    }

    @Override
    public void init(final FilterConfig filterConfig)
    {
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(String path, Microservlet servlet)
    {
        pathToServlet.put("/api/" + Application.get().version() + "/" + path, servlet);
    }

    /**
     * Handles GET and POST methods to the given microservlet.
     *
     * @param method The method, either GET or POST
     * @param cycle The request cycle
     * @param servlet The microservlet to call
     */
    private void handleRequest(final String method,
                               final MicroservletRequestCycle cycle,
                               final Microservlet servlet)
    {
        // Attach the request cycle to the microservlet and vice versa,
        servlet.attach(cycle);
        cycle.attach(servlet);

        // and if the request method is
        switch (method)
        {
            case "POST":
            {
                // then get the posted object,
                var request = cycle.request().object(servlet.responseType());

                // and respond with the object returned from onPost.
                cycle.response().object(servlet.onPost(request));
            }
            break;

            case "GET":
            {
                // then respond with the object returned from onGet.
                cycle.response().object(servlet.onGet());
            }
            break;

            case "PUT":
            case "DELETE":
                unsupported();
                break;
        }

        // Detach the request cycle.
        servlet.detach();
    }

    /**
     * Resolves the given file path to a microservice. Paths are resolved to the most specific match by removing the
     * last component of the path until a microservlet is found.
     *
     * @param path The mount path
     * @return The microservlet at the given path, or null if the path does not map to any microservlet
     */
    private Microservlet resolve(FilePath path)
    {
        for (; path.isNonEmpty(); path = path.withoutLast())
        {
            var servlet = pathToServlet.get(path);
            if (servlet != null)
            {
                return servlet;
            }
        }

        return null;
    }
}

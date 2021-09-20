package com.telenav.kivakit.microservice.rest.microservlet.jetty.filter;

import com.google.gson.Gson;
import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.resource.path.FilePath;
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
public class JettyMicroservletFilter implements Filter, ComponentMixin
{
    /** Map from path to microservlet */
    @UmlAggregation(referent = Microservlet.class, label = "mounts on paths", referentCardinality = "many")
    private final Map<FilePath, Microservlet<?, ?>> pathToMicroservlet = new HashMap<>();

    /** The microservice rest application */
    @UmlAggregation
    private final MicroserviceRestApplication restApplication;

    /**
     * @param restApplication The REST application that is using this filter
     */
    public JettyMicroservletFilter(final MicroserviceRestApplication restApplication)
    {
        this.restApplication = restApplication;
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

        // create microservlet request cycle,
        final var cycle = listenTo(new JettyMicroservletRequestCycle(restApplication, httpRequest, httpResponse));

        // resolve the microservlet at the requested path,
        final var microservlet = resolve(cycle.request().path());
        if (microservlet != null)
        {
            try
            {
                // and attach the request cycle to the (stateless) microservlet via thread-local map.
                handleRequest(httpRequest.getMethod(), cycle, microservlet);
            }
            catch (final Exception e)
            {
                problem(e, "REST $ method to $ failed", httpRequest.getMethod(), microservlet.objectName());
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
                warning(e, "Exception thrown by filter chain");
            }
        }
    }

    @Override
    public void init(final FilterConfig filterConfig)
    {
    }

    public Microservlet<?, ?> microservlet(final FilePath path)
    {
        return pathToMicroservlet.get(path);
    }

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    public final void mount(final String path, final Microservlet<?, ?> microservlet)
    {
        pathToMicroservlet.put(FilePath.parseFilePath("/api/" + Application.get().version() + "/" + path), microservlet);
    }

    public Set<FilePath> paths()
    {
        return pathToMicroservlet.keySet();
    }

    public MicroserviceRestApplication restApplication()
    {
        return restApplication;
    }

    /**
     * Handles GET and POST methods to the given microservlet.
     *
     * @param method The method, either GET or POST
     * @param cycle The request cycle
     * @param microservlet The microservlet to call
     */
    private void handleRequest(final String method,
                               final JettyMicroservletRequestCycle cycle,
                               final Microservlet<?, ?> microservlet)
    {
        // Attach the request cycle to the microservlet and vice versa,
        microservlet.attach(cycle);
        cycle.attach(microservlet);

        // and if the request method is
        switch (method)
        {
            case "POST":
            {
                // then convert the posted JSON to an object of the request type,
                final var request = listenTo(cycle.request().readObject(microservlet.requestType()));

                // and respond with the object returned from onPost.
                cycle.response().writeObject(microservlet.post(request));
            }
            break;

            case "GET":
            {
                // then turn parameters into a JSON object and then treat that like it was POSTed.
                final var json = cycle.request().parameters().asJson();
                final var request = cycle.gson().fromJson(json, microservlet.requestType());

                // Respond with the object returned from onGet.
                cycle.response().writeObject(microservlet.get(request));
            }
            break;

            case "PUT":
            case "DELETE":
                unsupported();
                break;
        }

        // Detach the request cycle.
        microservlet.detach();
    }

    /**
     * Resolves the given file path to a microservice. Paths are resolved to the most specific match by removing the
     * last component of the path until a microservlet is found.
     *
     * @param path The mount path
     * @return The microservlet at the given path, or null if the path does not map to any microservlet
     */
    private Microservlet<?, ?> resolve(FilePath path)
    {
        for (; path.isNonEmpty(); path = path.withoutLast())
        {
            final var microservlet = microservlet(path);
            if (microservlet != null)
            {
                return microservlet;
            }
        }

        return null;
    }
}

package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.internal.cycle.ProblemReportingTrait;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.Metric;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.MetricReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Represents a request / response cycle for a microservlet.
 * </p>
 *
 * <p><b>Request and Response</b></p>
 *
 * <p>
 * Provides the {@link JettyMicroservletRequest} and {@link JettyMicroserviceResponse} via {@link #request()} and {@link
 * #response()}. Also provides a {@link Gson} instance created by the application's factory for serialization of request
 * and response objects.
 * </p>
 *
 * <p><b>Microservlet Binding</b></p>
 *
 * <p>
 * The {@link #attach(Microservlet)} method is called by {@link JettyMicroservletFilter} to attach the {@link
 * Microservlet} handling this request cycle. The {@link Microservlet} cannot be a parameter to the constructor because
 * the request cycle is used to resolve the microservlet based on the request path.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletRequestCycle extends BaseComponent implements ProblemReportingTrait
{
    /** A thread local variable holding the request cycle for a given thread using this servlet */
    private static final ThreadLocal<JettyMicroservletRequestCycle> cycle = new ThreadLocal<>();

    /**
     * Attaches a request cycle to the current thread. This allows the cycle to be looked up anywhere in the code, as
     * some code does not have a request cycle parameter.
     */
    public static void attach(final JettyMicroservletRequestCycle cycle)
    {
        JettyMicroservletRequestCycle.cycle.set(cycle);
    }

    /**
     * @return The request cycle for the calling thread
     */
    public static JettyMicroservletRequestCycle cycle()
    {
        return cycle.get();
    }

    /**
     * Detaches any request cycle from this thread
     */
    public static void detach()
    {
        attach((JettyMicroservletRequestCycle) null);
    }

    /** The REST application that owns this request cycle */
    private final MicroserviceRestApplication application;

    /** The microservlet that is attached to this request cycle via {@link #attach(Microservlet)} */
    private Microservlet<?, ?> servlet;

    /** List of reported metrics for this request cycle */
    private final List<Metric<?>> metrics = new ArrayList<>(16);

    /** The request */
    @UmlAggregation
    private final JettyMicroservletRequest request;

    /** The response **/
    @UmlAggregation
    private final JettyMicroserviceResponse response;

    /** A Gson instance provided by the {@link MicroserviceRestApplication}'s factory */
    private final Lazy<Gson> gson = Lazy.of(() -> application().gsonFactory().newInstance());

    /**
     * @param application The REST application that owns this request cycle
     * @param request The Java Servlet API HTTP request object
     * @param response The Java Servlet API HTTP response object
     */
    public JettyMicroservletRequestCycle(final MicroserviceRestApplication application,
                                         final HttpServletRequest request,
                                         final HttpServletResponse response)
    {
        this.application = application;
        this.request = listenTo(new JettyMicroservletRequest(this, request));
        this.response = listenTo(new JettyMicroserviceResponse(this, response));
    }

    /**
     * Adds the given metric to this request cycle
     */
    public void add(final Metric<?> metric)
    {
        metrics.add(metric);
    }

    /**
     * @return The REST application that owns this request cycle
     */
    public MicroserviceRestApplication application()
    {
        return application;
    }

    /**
     * Binds the given servlet to this request cycle. The servlet cannot be a parameter to the constructor because the
     * request cycle is required to determine which servlet is bound to the request.
     *
     * @param servlet The servlet to attach
     */
    public void attach(final Microservlet<?, ?> servlet)
    {
        this.servlet = servlet;
    }

    /**
     * @return A Gson instance provided by the REST application
     */
    public Gson gson()
    {
        return gson.get();
    }

    /**
     * Sends metrics to the metric service
     */
    public void reportMetrics()
    {
        var reporter = lookup(MetricReporter.class);
        if (reporter != null)
        {
            reporter.report(metrics);
        }
    }

    /**
     * @return The request
     */
    public JettyMicroservletRequest request()
    {
        return request;
    }

    /**
     * @return The response
     */
    public JettyMicroserviceResponse response()
    {
        return response;
    }

    /**
     * @return The {@link Microservlet} handling this request cycle
     */
    public Microservlet<?, ?> servlet()
    {
        return servlet;
    }
}
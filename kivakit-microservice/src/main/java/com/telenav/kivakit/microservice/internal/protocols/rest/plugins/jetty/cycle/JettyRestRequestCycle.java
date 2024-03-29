package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.google.gson.Gson;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Represents an HTTP request / response cycle for a microservlet running on Jetty.
 * </p>
 *
 * <p><b>Request and Response</b></p>
 *
 * <p>
 * Provides the {@link JettyRestRequest} and {@link JettyRestResponse} via {@link #restRequest()} and
 * {@link #restResponse()}. Also provides a {@link Gson} instance created by the application's factory for serialization
 * of request and response objects.
 * </p>
 *
 * <p><b>Microservlet Binding</b></p>
 *
 * <p>
 * The {@link #attach(Microservlet)} method is called by {@link JettyMicroservletFilter} to attach the
 * {@link Microservlet} handling this request cycle. The {@link Microservlet} cannot be a parameter to the constructor
 * because the request cycle is used to resolve the microservlet based on the request path.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramJetty.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_SERVICE_PROVIDER)
public class JettyRestRequestCycle extends BaseComponent implements
    RestRequestCycle,
    RestProblemReportingTrait
{
    /** The REST application that owns this request cycle */
    private final RestService restService;

    /** The request */
    @UmlAggregation
    private final JettyRestRequest request;

    /** The response **/
    @UmlAggregation
    private final JettyRestResponse response;

    /** The microservlet that is attached to this request cycle via {@link #attach(Microservlet)} */
    private Microservlet<?, ?> servlet;

    /**
     * @param restService The REST application that owns this request cycle
     * @param request The Java Servlet API HTTP request object
     * @param response The Java Servlet API HTTP response object
     */
    public JettyRestRequestCycle(RestService restService,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    {
        this.restService = restService;
        this.request = listenTo(new JettyRestRequest(this, request));
        this.response = listenTo(new JettyRestResponse(this, response));
    }

    /**
     * Binds the given servlet to this request cycle. The servlet cannot be a parameter to the constructor because the
     * request cycle is required to determine which servlet is bound to the request.
     *
     * @param servlet The servlet to attach
     */
    public void attach(Microservlet<?, ?> servlet)
    {
        this.servlet = servlet;
    }

    /**
     * Returns the {@link Microservlet} handling this request cycle
     */
    @Override
    public Microservlet<?, ?> microservlet()
    {
        return servlet;
    }

    /**
     * Returns the request
     */
    @Override
    public RestRequest restRequest()
    {
        return request;
    }

    /**
     * Returns the response
     */
    @Override
    public RestResponse restResponse()
    {
        return response;
    }

    /**
     * Returns the REST application that owns this request cycle
     */
    @Override
    public RestService restService()
    {
        return restService;
    }
}

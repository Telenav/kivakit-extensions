package com.telenav.kivakit.microservice.rest.microservlet.cycle;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.JettyMicroservletFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a request / response cycle for a microservlet.
 * <p>
 * Provides the {@link MicroservletRequest} and {@link MicroserviceResponse} via {@link #request()} and {@link
 * #response()}. Also provides a {@link Gson} instance created by the application's factory for serialization of request
 * and response objects.
 * </p>
 *
 * <p><b>Binding</b></p>
 * <p>
 * The {@link #attach(Microservlet)} method is called by {@link JettyMicroservletFilter} to attach the {@link
 * Microservlet} handling this request cycle. The {@link Microservlet} cannot be a parameter to the constructor because
 * the request cycle is used to resolve the microservlet based on the request path.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRequestCycle extends BaseComponent
{
    /** The REST application that owns this request cycle */
    private final MicroserviceRestApplication application;

    /** The microservlet that is attached to this request cycle via {@link #attach(Microservlet)} */
    private Microservlet<?, ?> servlet;

    /** The request */
    private final MicroservletRequest request;

    /** The response **/
    private final MicroserviceResponse response;

    /** A Gson instance provided by the {@link MicroserviceRestApplication}'s factory */
    private final Lazy<Gson> gson = Lazy.of(() -> application().gsonFactory().newInstance());

    /**
     * @param application The REST application that owns this request cycle
     * @param request The Java Servlet API HTTP request object
     * @param response The Java Servlet API HTTP response object
     */
    public MicroservletRequestCycle(final MicroserviceRestApplication application,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response)
    {
        this.application = application;
        this.request = listenTo(new MicroservletRequest(this, request));
        this.response = listenTo(new MicroserviceResponse(this, response));
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
     * @return The request
     */
    public MicroservletRequest request()
    {
        return request;
    }

    /**
     * @return The response
     */
    public MicroserviceResponse response()
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

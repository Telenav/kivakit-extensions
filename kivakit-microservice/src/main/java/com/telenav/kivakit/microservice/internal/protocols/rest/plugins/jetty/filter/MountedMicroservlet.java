package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

/**
 * A mounted {@link Microservlet} which can handle requests with {@link #handleRequest(MicroserviceRestService.HttpMethod,
 * JettyMicroservletRequestCycle)}
 *
 * @author jonathanl (shibo)
 */
public class MountedMicroservlet extends Mounted
{
    public MountedMicroservlet(final MicroserviceRestService service)
    {
        super(service);
    }

    /**
     * Handles GET and POST methods to the given microservlet.
     *
     * @param method The HTTP request method
     * @param cycle The request cycle
     */
    @SuppressWarnings("ClassEscapesDefinedScope")
    public void handleRequest(MicroserviceRestService.HttpMethod method,
                              JettyMicroservletRequestCycle cycle)
    {
        measure(path, () ->
        {
            // Get the response object, microservlet, path and parameters,
            var response = cycle.response();
            var requestType = microservlet.requestType();
            var parameters = cycle.request().parameters(this.parameters.path());

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
                        service().onRequesting(request, method);
                        response.writeObject(microservlet.request(request));
                        service().onRequested(request, method);
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
                        service().onRequesting(request, method);
                        response.writeObject(microservlet.request(request));
                        service().onRequested(request, method);
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
        });
    }

    public Microservlet<?, ?> microservlet()
    {
        return microservlet;
    }

    /** The microservlet */
    Microservlet<?, ?> microservlet;

    /** The path to the microservlet */
    MicroservletRestPath path;

    /** Any path parameters */
    MicroservletRestPath parameters;
}

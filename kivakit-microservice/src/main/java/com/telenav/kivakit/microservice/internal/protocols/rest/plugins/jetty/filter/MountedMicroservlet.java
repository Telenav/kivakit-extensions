package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.http.RestPath;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.properties.PropertyMap;

/**
 * A mounted {@link Microservlet} which can handle requests with
 * {@link #handleRequest(HttpMethod, RestRequestCycle)}
 *
 * @author jonathanl (shibo)
 */
public class MountedMicroservlet extends Mounted
{
    /** The microservlet */
    Microservlet<?, ?> microservlet;

    /** The path to the microservlet */
    RestPath path;

    /** Any path parameters */
    RestPath parameters;

    public MountedMicroservlet(final RestService service)
    {
        super(service);
    }

    /**
     * Handles GET and POST methods to the given microservlet.
     *
     * @param method The HTTP request method
     * @param cycle The request cycle
     */
    public void handleRequest(HttpMethod method,
                              RestRequestCycle cycle)
    {
        measure(path, () ->
        {
            // Get the response object, microservlet, path and parameters,
            var response = cycle.restResponse();
            var requestType = microservlet.requestType();
            var parameters = cycle.restRequest().parameters(this.parameters.path());

            // and if the request method is
            switch (method)
            {
                case POST:
                {
                    handlePost(method, cycle, response, requestType, parameters);
                }
                break;

                case GET:
                case DELETE:
                {
                    handleGetDelete(method, cycle, response, requestType, parameters);
                }
                break;

                default:
                    response.problem(HttpStatus.METHOD_NOT_ALLOWED, "Method $ not supported", method.name());
                    break;
            }
        });
    }

    /**
     * @return The microservlet associated with this mount
     */
    public Microservlet<?, ?> microservlet()
    {
        return microservlet;
    }

    private void handleGetDelete(HttpMethod method,
                                 RestRequestCycle cycle,
                                 RestResponse response,
                                 Class<? extends MicroservletRequest> requestType,
                                 PropertyMap parameters)
    {
        // then turn parameters into a JSON object and then treat that like it was POSTed.
        var request = cycle.gson().fromJson(parameters.asJson(), requestType);

        // Respond with the object returned from onGet.
        if (request != null)
        {
            listenTo(request);
            restService().onRequesting(request, method);
            response.writeResponse(microservlet.respond(request));
            restService().onRequested(request, method);
        }
        else
        {
            response.problem(HttpStatus.BAD_REQUEST, "Bad request: unable to deserialize");
        }
    }

    private void handlePost(HttpMethod method,
                            RestRequestCycle cycle,
                            RestResponse response,
                            Class<? extends MicroservletRequest> requestType,
                            PropertyMap parameters)
    {
        // If there is a request body,
        MicroservletRequest request;
        if (cycle.restRequest().hasBody())
        {
            // then convert the JSON in the body to a request object,
            request = cycle.restRequest().readRequest(requestType);
        }
        else
        {
            // otherwise, convert any parameters to a JSON request object,
            request = cycle.gson().fromJson(parameters.asJson(), requestType);
        }

        // Respond with the object returned from respond(request).
        if (request != null)
        {
            listenTo(request);
            restService().onRequesting(request, method);
            response.writeResponse(microservlet.respond(request));
            restService().onRequested(request, method);
        }
        else
        {
            response.problem(HttpStatus.BAD_REQUEST, "Bad request: unable to deserialize");
        }
    }
}

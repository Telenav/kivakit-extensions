package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestPath;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializer;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializers;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.properties.PropertyMap;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.network.http.HttpStatus.BAD_REQUEST;
import static com.telenav.kivakit.network.http.HttpStatus.METHOD_NOT_ALLOWED;

/**
 * A mounted {@link Microservlet} which can handle requests with {@link #handleRequest(HttpMethod, RestRequestCycle)}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class MountedMicroservlet extends BaseMounted
{
    /** The microservlet */
    Microservlet<?, ?> microservlet;

    /** The path to the microservlet */
    RestPath path;

    /** Any path parameters */
    RestPath parameters;

    public MountedMicroservlet(RestService service)
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
                case POST -> handlePost(method, cycle, response, requestType, parameters);
                case GET, DELETE -> handleGetDelete(method, cycle, response, requestType, parameters);
                default -> response.problem(METHOD_NOT_ALLOWED, "Method $ not supported", method.name());
            }
        });
    }

    /**
     * Returns the microservlet associated with this mount
     */
    public Microservlet<?, ?> microservlet()
    {
        return microservlet;
    }

    public RestPath path()
    {
        return path;
    }

    @SuppressWarnings("unused")
    private <T extends MicroservletRequest> void
    handleGetDelete(HttpMethod method,
                    RestRequestCycle cycle,
                    RestResponse response,
                    Class<T> requestType,
                    PropertyMap parameters)
    {
        // then turn parameters into a JSON object and then treat that like it was POSTed.
        var request = restSerializer(requestType).deserializeRequest(parameters, requestType);

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
            response.problem(BAD_REQUEST, "Bad request: unable to deserialize");
        }
    }

    private <Request extends MicroservletRequest, Response extends MicroservletResponse> void
    handlePost(HttpMethod method,
               RestRequestCycle cycle,
               RestResponse response,
               Class<Request> requestType,
               PropertyMap parameters)
    {
        // If there is a request body,
        MicroservletRequest request;
        if (cycle.restRequest().hasBody())
        {
            // then convert the text body to a request object,
            request = cycle.restRequest().readRequest(requestType);
        }
        else
        {
            // otherwise, convert any parameters to a request object,
            RestSerializer<Request, Response> restSerializer = restSerializer(requestType);
            request = restSerializer.deserializeRequest(parameters, requestType);
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
            response.problem(BAD_REQUEST, "Bad request: unable to deserialize");
        }
    }

    /**
     * Returns the appropriate {@link RestSerializer} for the given request type. If the type itself does not define
     * <i>public static RestSerializer restSerializer()</i>, then the return value of {@link #defaultRestSerializer()}
     * method is used.
     *
     * @param requestType The request type
     * @return The {@link RestSerializer}
     */
    @SuppressWarnings("unchecked")
    private <Request extends MicroservletRequest, Response extends MicroservletResponse>
    RestSerializer<Request, Response> restSerializer(Class<Request> requestType)
    {
        var serializer = RestSerializers.restSerializer(requestType);
        return (RestSerializer<Request, Response>) (serializer == null ? defaultRestSerializer() : serializer);
    }
}

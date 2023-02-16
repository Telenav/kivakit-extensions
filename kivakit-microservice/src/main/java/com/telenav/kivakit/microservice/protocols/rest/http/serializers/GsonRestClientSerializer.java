package com.telenav.kivakit.microservice.protocols.rest.http.serializers;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestClientSerializer;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.serialization.gson.GsonFactorySource;

/**
 * A client-side serializer that uses {@link Gson} to serialize requests and deserialize responses
 *
 * @author Jonathan Locke
 */
public class GsonRestClientSerializer<Request, Response> extends BaseComponent implements RestClientSerializer<Request, Response>
{
    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String contentType()
    {
        return "application/json";
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public MicroservletErrorResponse deserializeErrors(String text, Class<MicroservletErrorResponse> type)
    {
        return gson(null).fromJson(text, type);
    }

    /**
     * {@inheritDoc}
     *
     * @param text {@inheritDoc}
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Response deserializeResponse(String text, Class<Response> type)
    {
        return gson(null).fromJson(text, type);
    }

    /**
     * {@inheritDoc}
     *
     * @param object {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String serializeRequest(Request object)
    {
        return gson(object).toJson(object);
    }

    /**
     * Provides a {@link Gson} serializer for the given object. If the object implements {@link GsonFactorySource}, that
     * interface provides the serializer, otherwise, the {@link Gson} instance is provided by the request cycle
     *
     * @param object The object
     * @return The {@link Gson} serializer for the object
     */
    private Gson gson(Object object)
    {
        // If the response object has a custom GsonFactory,
        if (object instanceof GsonFactorySource)
        {
            // use that to convert the response to JSON,
            return ((GsonFactorySource) object).gsonFactory().gson();
        }
        else
        {
            // otherwise, use the GsonFactory, provided by the application through the request cycle.
            return require(GsonFactory.class).gson();
        }
    }
}

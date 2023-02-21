package com.telenav.kivakit.microservice.protocols.rest.http.serializers;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializer;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.serialization.gson.GsonFactorySource;

import java.io.PrintWriter;
import java.io.Reader;

/**
 * A serializer that uses {@link Gson} to serializer REST requests and responses
 *
 * @author Jonathan Locke
 */
public class GsonRestSerializer<Request extends MicroservletRequest, Response extends MicroservletResponse>
    extends BaseComponent implements RestSerializer<Request, Response>
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
     * Deserializes the given request object to text using the application's registered {@link GsonFactory}. This
     * behavior can be overridden by implementing {@link GsonFactorySource} on the response object to provide a custom
     * {@link GsonFactory} for that response object.
     *
     * @param in The text object to be deserialized into a request object
     * @return The object serialized into JSON format,
     */
    @Override
    public Request deserializeRequest(Reader in, Class<Request> requestType)
    {
        return gson(null).fromJson(in, requestType);
    }

    @Override
    public Request deserializeRequest(PropertyMap properties, Class<Request> requestType)
    {
        return gson(null).fromJson(properties.toJson(), requestType);
    }

    @Override
    public void serializeErrors(PrintWriter out, MicroservletErrorResponse errors)
    {
        gson(errors).toJson(errors, out);
    }

    /**
     * Serializes the given response object to text using the application's registered {@link GsonFactory}. This
     * behavior can be overridden by implementing {@link GsonFactorySource} on the response object to provide a custom
     * {@link GsonFactory} for that response object.
     *
     * @param response The response object to be serialized to text
     */
    @Override
    public void serializeResponse(PrintWriter out, Response response)
    {
        gson(response).toJson(response, out);
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

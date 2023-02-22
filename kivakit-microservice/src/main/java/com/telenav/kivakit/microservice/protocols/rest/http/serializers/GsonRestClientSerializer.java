package com.telenav.kivakit.microservice.protocols.rest.http.serializers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestClientSerializer;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.serialization.gson.GsonFactorySource;

import java.io.PrintWriter;
import java.io.Reader;

/**
 * A client-side serializer that uses {@link Gson} to serialize requests and deserialize responses
 *
 * @author Jonathan Locke
 */
public class GsonRestClientSerializer<Request extends MicroservletRequest, Response extends MicroservletResponse>
    extends BaseComponent implements RestClientSerializer<Request, Response>
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
     * @param in {@inheritDoc}
     * @param type {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Response deserializeResponse(Reader in, Class<Response> type)
    {
        try
        {
            var jsonReader = new JsonReader(in);
            return gson(null).fromJson(jsonReader, type);
        }
        catch (Exception e)
        {
            problem(e, "Unable to deserialize: $", type);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param out {@inheritDoc}
     * @param object {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public void serializeRequest(PrintWriter out, Request object)
    {
        gson(object).toJson(object, out);
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

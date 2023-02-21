package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.properties.PropertyMap;

import java.io.PrintWriter;
import java.io.Reader;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Server-side serializer that deserializes requests and serializes responses.
 *
 * @author Jonathan Locke
 */
public interface RestSerializer<Request extends MicroservletRequest, Response extends MicroservletResponse>
{
    /**
     * Returns the content type of the serialized data, such as "application/json"
     *
     * @return The content type
     */
    String contentType();

    /**
     * Deserializes an object of the given type from the given properties
     *
     * @param properties The properties from the query parameters of an HTTP request
     * @param requestType The type of object
     * @return The deserialized object
     */
    default Request deserializeRequest(PropertyMap properties, Class<Request> requestType)
    {
        return unsupported();
    }

    /**
     * Deserializes an object of the given type from the given input
     *
     * @param in The input to deserialize
     * @param requestType The type of object
     * @return The deserialized object
     */
    default Request deserializeRequest(Reader in, Class<Request> requestType)
    {
        return unsupported();
    }

    /**
     * Serializes the given errors to text and writes them to the given output
     *
     * @param out The output to write to
     * @param errors The errors to write
     */
    default void serializeErrors(PrintWriter out, MicroservletErrorResponse errors)
    {
        unsupported();
    }

    /**
     * Serializes the given object to text and writes it to the given output
     *
     * @param out The output to write to
     * @param response The object to serialize
     */
    void serializeResponse(PrintWriter out, Response response);
}

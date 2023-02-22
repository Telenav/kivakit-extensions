package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

import java.io.PrintWriter;
import java.io.Reader;

/**
 * Client-side serializer that serializes requests and deserializes responses.
 *
 * @author Jonathan Locke
 */
public interface RestClientSerializer<Request extends MicroservletRequest, Response extends MicroservletResponse>
{
    /**
     * Returns the content type for this serializer, such as "application/json"
     *
     * @return The content type
     */
    String contentType();

    /**
     * Deserializes an object of the given type from the given input
     *
     * @param in The input to deserialize
     * @param type The type of object
     * @return The deserialized object
     */
    Response deserializeResponse(Reader in, Class<Response> type);

    /**
     * Serializes the given object to text and writes it to the given writer
     *
     * @param out The writer to write to
     * @param object The object to serialize
     */
    void serializeRequest(PrintWriter out, Request object);
}

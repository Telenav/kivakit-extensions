package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;

/**
 * Client-side serializer that serializes requests and deserializes responses.
 *
 * @author Jonathan Locke
 */
public interface RestClientSerializer<Request, Response>
{
    /**
     * Returns the content type of the serialized data, such as "application/json"
     *
     * @return The content type
     */
    String contentType();

    /**
     * Deserializes the given error text
     *
     * @param text The text to deserialize
     * @param type The type of object
     * @return The errors object
     */
    MicroservletErrorResponse deserializeErrors(String text, Class<MicroservletErrorResponse> type);

    /**
     * Deserializes from the given text to an object of the given type
     *
     * @param text The text to deserialize
     * @param type The type of object
     * @return The converted object
     */
    Response deserializeResponse(String text, Class<Response> type);

    /**
     * Serializes the given object to text
     *
     * @param object The object to serialize
     * @return The text for the object
     */
    String serializeRequest(Request object);
}

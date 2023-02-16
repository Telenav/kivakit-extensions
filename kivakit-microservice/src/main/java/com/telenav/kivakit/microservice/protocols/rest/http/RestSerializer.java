package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.properties.PropertyMap;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Server-side serializer that deserializes requests and serializes responses.
 *
 * @author Jonathan Locke
 */
public interface RestSerializer<Request, Response>
{
    /**
     * Returns the content type of the serialized data, such as "application/json"
     *
     * @return The content type
     */
    String contentType();

    /**
     * Deserializes from the given properties to an object of the given type
     *
     * @param properties The properties from an HTTP request
     * @param type The type of object
     * @return The converted object
     */
    default Request deserializeRequest(PropertyMap properties, Class<Request> type)
    {
        return unsupported();
    }

    /**
     * Deserializes from the given text to an object of the given type
     *
     * @param text The text to deserialize
     * @param type The type of object
     * @return The converted object
     */
    default Request deserializeRequest(String text, Class<Request> type)
    {
        return unsupported();
    }

    /**
     * Serializes the given errors to text
     *
     * @param errors The errors
     * @return The serialized text
     */
    default String serializeErrors(MicroservletErrorResponse errors)
    {
        return unsupported();
    }

    /**
     * Serializes the given object to text
     *
     * @param object The object to serialize
     * @return The text for the object
     */
    String serializeResponse(Response object);
}

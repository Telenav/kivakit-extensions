package com.telenav.kivakit.microservice.protocols.rest.http;

/**
 * Interface that allows use of different kinds of serializers for rest requests and responses
 *
 * @author Jonathan Locke
 */
public interface RestSerializer
{
    /**
     * Returns the content type of the serialized data, such as "application/json"
     *
     * @return The content type
     */
    String contentType();

    /**
     * Converts from the given text to the given type
     *
     * @param text The text to convert
     * @param type The type to convert to
     * @return The converted object
     */
    <T> T deserialize(String text, Class<T> type);

    /**
     * Converts from the given object to text
     *
     * @param object The object to convert
     * @return The text for the object
     */
    <T> String serialize(T object);
}

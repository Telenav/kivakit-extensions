package com.telenav.kivakit.microservice.protocols.rest.gson;

import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.JettyOpenApiRequest;

/**
 * Interface that allows a response object to serialize a different object as the JSON response.
 *
 * @author jonathanl (shibo)
 * @see JettyOpenApiRequest
 */
public interface MicroserviceGsonObjectSource
{
    Object gsonObject();
}

package com.telenav.kivakit.microservice.microservlet.rest;

import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.openapi.JettyOpenApiRequest;

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

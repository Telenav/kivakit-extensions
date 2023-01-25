package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;

/**
 * Response to OpenAPI request
 */
public class OpenApiResponse extends BaseMicroservletResponse implements MicroserviceGsonObjectSource
{
    @Expose
    private final String api;

    public OpenApiResponse(Listener listener)
    {
        api = listener.listenTo(new OpenApiBuilder()).build().toString();
    }

    @Override
    public Object gsonObject()
    {
        return api;
    }
}

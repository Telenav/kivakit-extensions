package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializer;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiBuilder.openApiBuilder;

/**
 * Response to OpenAPI request
 */
public class OpenApiResponse extends BaseMicroservletResponse implements RestSerializer
{
    @Expose
    private final String api;

    public OpenApiResponse(Listener listener)
    {
        api = listener.listenTo(openApiBuilder()).buildYaml().toString();
    }

    @Override
    public String contentType()
    {
        return "text/yaml";
    }

    @Override
    public <T> T deserialize(String text, Class<T> type)
    {
        return unsupported();
    }

    @Override
    public <T> String serialize(T object)
    {
        return api;
    }
}

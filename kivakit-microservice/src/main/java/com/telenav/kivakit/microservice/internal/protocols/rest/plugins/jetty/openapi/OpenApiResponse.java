package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializer;

import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiBuilder.openApiBuilder;

/**
 * Response to OpenAPI request
 */
public class OpenApiResponse extends BaseMicroservletResponse
{
    public static RestSerializer<Object, OpenApiResponse> restSerializer()
    {
        return new RestSerializer<>()
        {
            @Override
            public String contentType()
            {
                return "text/yaml";
            }

            @Override
            public String serializeResponse(OpenApiResponse object)
            {
                return object.api;
            }
        };
    }

    private final String api;

    public OpenApiResponse(Listener listener)
    {
        api = listener.listenTo(openApiBuilder()).buildYaml().toString();
    }
}

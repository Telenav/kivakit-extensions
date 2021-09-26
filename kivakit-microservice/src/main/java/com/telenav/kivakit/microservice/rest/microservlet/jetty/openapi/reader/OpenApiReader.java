package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeType;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiRequestHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Reads simplified OpenApi annotations from a {@link MicroserviceRestApplication}.
 *
 * <p><b>Annotations</b></p>
 * <ul>
 *     <li>@OpenApiExcludeMember - Excludes a member from a supertype</li>
 *     <li>@OpenApiIncludeMember - Includes a member from the current class or any supertype</li>
 *     <li>@OpenApiRequestHandler - Used to annotate onGet, onPost or onDelete request methods</li>
 *     <li>@OpenApiIncludeType - Used to describe a request or response class</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see OpenApiIncludeMember
 * @see OpenApiExcludeMember
 * @see OpenApiRequestHandler
 * @see OpenApiIncludeType
 */
public class OpenApiReader extends BaseComponent
{
    public OpenAPI read()
    {
        final var api = new OpenAPI();

        final var restApplication = require(MicroserviceRestApplication.class);
        final var schemaReader = listenTo(new OpenApiSchemaReader());
        final var pathReader = listenTo(new OpenApiPathReader());

        api.info(restApplication.openApiInfo());
        api.paths(pathReader.read());
        api.components(new Components().schemas(schemaReader.read()));

        return api;
    }
}

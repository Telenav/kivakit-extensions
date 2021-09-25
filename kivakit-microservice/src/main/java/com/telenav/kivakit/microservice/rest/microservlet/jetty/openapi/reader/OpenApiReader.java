package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExclude;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiInclude;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiRequest;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Reads simplified OpenApi annotations from a {@link MicroserviceRestApplication}.
 *
 * <p><b>Annotations</b></p>
 * <ul>
 *     <li>@OpenApiExclude - Excludes a member from a supertype</li>
 *     <li>@OpenApiInclude - Includes a member from the current class or any supertype</li>
 *     <li>@OpenApiRequest - Used to annotate onGet, onPost or onDelete request methods</li>
 *     <li>@OpenApiSchema - Used to describe a request or response class</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see OpenApiInclude
 * @see OpenApiExclude
 * @see OpenApiRequest
 * @see OpenApiSchema
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

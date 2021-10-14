package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiRequestHandler;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Reads simplified OpenApi annotations from a {@link MicroserviceRestService}.
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

        final var restApplication = require(MicroserviceRestService.class);
        final var schemaReader = listenTo(new OpenApiSchemaReader());
        final var pathReader = listenTo(new OpenApiPathReader());

        api.info(restApplication.openApiInfo());
        api.paths(pathReader.read());
        api.components(new Components().schemas(schemaReader.read()));

        return api;
    }
}

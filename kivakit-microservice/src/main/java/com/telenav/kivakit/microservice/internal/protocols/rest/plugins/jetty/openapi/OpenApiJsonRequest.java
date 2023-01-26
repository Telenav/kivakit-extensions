package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyPlugin;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.models.OpenAPI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletRequest} handler that produces an {@link OpenAPI} definition for a
 * {@link MicroservletJettyPlugin}.
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramJetty.class)
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class OpenApiJsonRequest extends BaseMicroservletRequest
{
    /**
     * Returns responds to a GET request with the OpenAPI definition for the {@link RestService}.
     */
    @Override
    public MicroservletResponse onRespond()
    {
        return listenTo(new OpenApiResponse(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return OpenApiResponse.class;
    }
}

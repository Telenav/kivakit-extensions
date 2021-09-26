package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.OpenApiReader;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.serialization.OpenApiSerializer;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletGetRequest} request that produces an {@link OpenAPI} definition for a {@link
 * MicroservletJettyFilterPlugin}.
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
@OpenApiExcludeMember({ "exampleSetFlag" })
public class JettyOpenApiRequest extends MicroservletGetRequest
{
    /**
     * Response to OpenAPI request
     */
    public static class JettyOpenApiResponse extends MicroservletResponse
    {
        @SuppressWarnings("FieldCanBeLocal")
        @Expose
        private final OpenAPI api;

        public JettyOpenApiResponse(Listener listener)
        {
            api = listener.listenTo(new OpenApiReader()).read();
        }

        @Override
        public String toJson()
        {
            return new OpenApiSerializer().toJson(api);
        }
    }

    /**
     * @return Responds to a GET request with the OpenAPI definition for the {@link MicroserviceRestApplication}.
     */
    @Override
    public MicroservletResponse onGet()
    {
        return new JettyOpenApiResponse(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return JettyOpenApiResponse.class;
    }
}

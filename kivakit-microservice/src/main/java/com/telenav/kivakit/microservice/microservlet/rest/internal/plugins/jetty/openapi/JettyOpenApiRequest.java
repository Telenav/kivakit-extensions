package com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.openapi;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceGsonFactorySource;
import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.openapi.reader.OpenApiReader;
import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.openapi.serialization.OpenApiGsonFactory;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletRequest} handler that produces an {@link OpenAPI} definition for a {@link
 * MicroservletJettyFilterPlugin}.
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
@OpenApiExcludeMember({ "exampleSetFlag" })
public class JettyOpenApiRequest extends BaseMicroservletRequest
{
    /**
     * Response to OpenAPI request
     */
    public static class JettyOpenApiResponse extends BaseMicroservletResponse implements
            MicroserviceGsonFactorySource,
            MicroserviceGsonObjectSource
    {
        @SuppressWarnings("FieldCanBeLocal")
        @Expose
        private final OpenAPI api;

        public JettyOpenApiResponse(Listener listener)
        {
            api = listener.listenTo(new OpenApiReader()).read();
        }

        @Override
        public GsonFactory gsonFactory()
        {
            return new OpenApiGsonFactory();
        }

        @Override
        public Object gsonObject()
        {
            return api;
        }
    }

    /**
     * @return Responds to a GET request with the OpenAPI definition for the {@link MicroserviceRestService}.
     */
    @Override
    public MicroservletResponse onRequest()
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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyPlugin;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.OpenApiReader;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.ArraySerializer;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.ListSerializer;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.MapSerializer;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.SetSerializer;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.StringSerializer;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.project.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.serialization.json.GsonFactorySource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletRequest} handler that produces an {@link OpenAPI} definition for a {@link
 * MicroservletJettyPlugin}.
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
@OpenApiExcludeMember({ "exampleSetFlag" })
public class OpenApiJsonRequest extends BaseMicroservletRequest
{
    /**
     * Response to OpenAPI request
     */
    public static class JettyOpenApiResponse extends BaseMicroservletResponse implements
            GsonFactorySource,
            MicroserviceGsonObjectSource
    {
        @Expose
        private final OpenAPI api;

        public JettyOpenApiResponse(Listener listener)
        {
            api = listener.listenTo(new OpenApiReader()).read();
        }

        @Override
        public GsonFactory gsonFactory()
        {
            var factory = JettyMicroservletRequestCycle.cycle()
                    .application()
                    .gsonFactory();

            return factory.withPrettyPrinting(true)
                    .withRequireExposeAnnotation(false)
                    .withSerializeNulls(false)
                    .withSerializer(List.class, new ListSerializer())
                    .withSerializer(Set.class, new SetSerializer())
                    .withSerializer(Map.class, new MapSerializer())
                    .withSerializer(Object[].class, new ArraySerializer<>())
                    .withSerializer(String.class, new StringSerializer())
                    .withExclusionStrategy(new ExclusionStrategy()
                    {
                        @Override
                        public boolean shouldSkipClass(Class<?> clazz)
                        {
                            return false;
                        }

                        @Override
                        public boolean shouldSkipField(FieldAttributes field)
                        {
                            return field.getName().equals("exampleSetFlag");
                        }
                    });
        }

        @Override
        public Object gsonObject()
        {
            return api;
        }
    }

    @Expose
    private String ignored = "IGNORED";

    /**
     * @return Responds to a GET request with the OpenAPI definition for the {@link MicroserviceRestService}.
     */
    @Override
    public MicroservletResponse onRespond()
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
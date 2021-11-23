package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.dyuproject.protostuff.Tag;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyFilterPlugin;
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
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.serialization.json.DefaultGsonFactory;
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
            GsonFactorySource,
            MicroserviceGsonObjectSource
    {
        @Tag(1)
        @Expose
        private final OpenAPI api;

        public JettyOpenApiResponse(Listener listener)
        {
            api = listener.listenTo(new OpenApiReader()).read();
        }

        @Override
        public GsonFactory gsonFactory()
        {
            var factory = (DefaultGsonFactory) JettyMicroservletRequestCycle.cycle()
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

    @Tag(1)
    @Expose
    private String ignored = "IGNORED";

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

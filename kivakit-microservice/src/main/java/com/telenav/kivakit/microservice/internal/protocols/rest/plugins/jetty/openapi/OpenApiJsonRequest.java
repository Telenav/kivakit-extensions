package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyPlugin;
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
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequestThread;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.serialization.gson.factory.GsonFactory;
import com.telenav.kivakit.serialization.gson.factory.GsonFactorySource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletRequest} handler that produces an {@link OpenAPI} definition for a
 * {@link MicroservletJettyPlugin}.
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramJetty.class)
@OpenApiExcludeMember({ "exampleSetFlag" })
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public GsonFactory gsonFactory()
        {
            var factory = RestRequestThread.requestCycle()
                    .restService()
                    .microservice()
                    .gsonFactory();

            return factory.prettyPrinting(true)
                    .requireExposeAnnotation(false)
                    .serializeNulls(false)
                    .addJsonSerializer(List.class, new ListSerializer())
                    .addJsonSerializer(Set.class, new SetSerializer())
                    .addJsonSerializer(Map.class, new MapSerializer())
                    .addJsonSerializer(Object[].class, new ArraySerializer<>())
                    .addJsonSerializer(String.class, new StringSerializer())
                    .exclusionStrategy(new ExclusionStrategy()
                    {
                        @Override
                        public boolean shouldSkipClass(Class<?> clazz)
                        {
                            return false;
                        }

                        @Override
                        public boolean shouldSkipField(FieldAttributes field)
                        {
                            return "exampleSetFlag".equals(field.getName());
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
     * Returns responds to a GET request with the OpenAPI definition for the {@link RestService}.
     */
    @Override
    public MicroservletResponse onRespond()
    {
        return listenTo(new JettyOpenApiResponse(this));
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

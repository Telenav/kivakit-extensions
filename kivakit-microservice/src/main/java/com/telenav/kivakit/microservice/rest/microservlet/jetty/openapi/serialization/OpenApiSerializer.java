package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.serialization.ArraySerializer;
import com.telenav.kivakit.microservice.rest.microservlet.serialization.ListSerializer;
import com.telenav.kivakit.microservice.rest.microservlet.serialization.MapSerializer;
import com.telenav.kivakit.microservice.rest.microservlet.serialization.StringSerializer;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.Collection;
import java.util.Map;

public class OpenApiSerializer
{
    public String toJson(OpenAPI api)
    {
        final var factory = JettyMicroservletRequestCycle.cycle()
                .application()
                .gsonFactory();

        final var builder = factory.builder();
        factory.addSerializers(builder);
        builder.setPrettyPrinting();
        builder.registerTypeHierarchyAdapter(Collection.class, new ListSerializer());
        builder.registerTypeAdapter(Object[].class, new ArraySerializer<>());
        builder.registerTypeAdapter(String.class, new StringSerializer());
        builder.registerTypeAdapter(Map.class, new MapSerializer());
        builder.setExclusionStrategies(new ExclusionStrategy()
        {
            @Override
            public boolean shouldSkipClass(final Class<?> clazz)
            {
                return false;
            }

            @Override
            public boolean shouldSkipField(final FieldAttributes field)
            {
                return field.getName().equals("exampleSetFlag");
            }
        });

        return builder.create().toJson(api);
    }
}

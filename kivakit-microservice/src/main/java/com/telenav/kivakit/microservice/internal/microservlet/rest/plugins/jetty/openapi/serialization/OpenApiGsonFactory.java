package com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.openapi.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.serialization.json.GsonFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenApiGsonFactory extends GsonFactory
{
    @Override
    public GsonBuilder builder()
    {
        final var factory = JettyMicroservletRequestCycle.cycle()
                .application()
                .gsonFactory();

        final var builder = factory.builder();
        factory.addSerializers(builder);
        builder.setPrettyPrinting();
        builder.registerTypeHierarchyAdapter(List.class, new ListSerializer());
        builder.registerTypeHierarchyAdapter(Set.class, new SetSerializer());
        builder.registerTypeHierarchyAdapter(Map.class, new MapSerializer());
        builder.registerTypeAdapter(Object[].class, new ArraySerializer<>());
        builder.registerTypeAdapter(String.class, new StringSerializer());
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

        return builder;
    }
}

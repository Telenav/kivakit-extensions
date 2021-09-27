package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Set;

public class SetSerializer implements JsonSerializer<Set<?>>
{
    @Override
    public JsonElement serialize(Set<?> set, Type typeOfSrc, JsonSerializationContext context)
    {
        if (set == null || set.isEmpty())
        {
            return null;
        }

        var array = new JsonArray();

        for (var at : set)
        {
            array.add(context.serialize(at));
        }

        return array;
    }
}
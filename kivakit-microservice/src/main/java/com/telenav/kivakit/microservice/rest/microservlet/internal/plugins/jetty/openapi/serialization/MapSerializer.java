package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class MapSerializer implements JsonSerializer<Map<?, ?>>
{
    @Override
    public JsonElement serialize(Map<?, ?> map, Type typeOfSrc, JsonSerializationContext context)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }

        var object = new JsonObject();

        for (var at : map.keySet())
        {
            object.add(at.toString(), context.serialize(map.get(at)));
        }

        return object;
    }
}
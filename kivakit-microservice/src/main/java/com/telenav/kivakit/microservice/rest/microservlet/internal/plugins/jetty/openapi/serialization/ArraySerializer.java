package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ArraySerializer<T> implements JsonSerializer<T[]>
{
    @Override
    public JsonElement serialize(T[] array, Type typeOfSrc, JsonSerializationContext context)
    {
        if (array == null || array.length == 0)
        {
            return null;
        }

        var json = new JsonArray();

        for (var at : array)
        {
            json.add(context.serialize(at));
        }

        return json;
    }
}
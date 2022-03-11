package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class ListSerializer<T> implements JsonSerializer<List<T>>
{
    @Override
    public JsonElement serialize(List<T> list, Type typeOfSrc, JsonSerializationContext context)
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }

        var array = new JsonArray();

        for (var at : list)
        {
            array.add(context.serialize(at));
        }

        return array;
    }
}
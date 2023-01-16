package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import java.lang.reflect.Type;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
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

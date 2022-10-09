package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.reflect.Type;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

@CodeQuality(stability = STABILITY_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
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

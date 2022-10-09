package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.reflect.Type;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

@CodeQuality(stability = STABILITY_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class SetSerializer<T> implements JsonSerializer<Set<T>>
{
    @Override
    public JsonElement serialize(Set<T> set, Type typeOfSrc, JsonSerializationContext context)
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

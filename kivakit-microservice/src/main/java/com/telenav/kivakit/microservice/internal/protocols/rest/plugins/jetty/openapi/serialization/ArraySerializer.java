package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.CodeQuality;

import java.lang.reflect.Type;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_UNSTABLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

@CodeQuality(stability = CODE_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_PRIVATE)
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

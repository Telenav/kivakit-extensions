package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.CodeQuality;

import java.lang.reflect.Type;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_UNSTABLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

@CodeQuality(stability = CODE_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_PRIVATE)
public class MapSerializer<Key, Value> implements JsonSerializer<Map<Key, Value>>
{
    @Override
    public JsonElement serialize(Map<Key, Value> map, Type typeOfSrc, JsonSerializationContext context)
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

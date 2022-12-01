package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import java.lang.reflect.Type;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
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

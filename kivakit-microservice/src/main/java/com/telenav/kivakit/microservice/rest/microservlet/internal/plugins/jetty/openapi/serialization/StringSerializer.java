package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class StringSerializer implements JsonSerializer<String>
{
    @Override
    public JsonElement serialize(String string, Type typeOfSrc, JsonSerializationContext context)
    {
        if (string == null || string.isEmpty())
        {
            return null;
        }

        return new JsonPrimitive(string);
    }
}
package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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

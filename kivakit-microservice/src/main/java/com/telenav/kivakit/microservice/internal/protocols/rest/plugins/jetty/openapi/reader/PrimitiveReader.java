package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.Type;
import io.swagger.v3.oas.models.media.Schema;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class PrimitiveReader
{
    /**
     * Populates the given schema object with a type and format if the type parameter is a primitive value class.
     *
     * @param type The type of object
     * @return The schema for the object if it is primitive or null if it is not
     */
    public Schema<?> readPrimitive(Type<?> type)
    {
        var schema = new Schema<>();
        if (type.is(String.class))
        {
            schema.type("string");
            return schema;
        }
        if (type.is(Double.class) || type.is(double.class))
        {
            schema.type("number");
            schema.format("double");
            return schema;
        }
        if (type.is(Float.class) || type.is(float.class))
        {
            schema.type("number");
            schema.format("float");
            return schema;
        }
        if (type.is(Long.class) || type.is(long.class))
        {
            schema.type("integer");
            schema.format("int64");
            return schema;
        }
        if (type.is(Integer.class) || type.is(int.class))
        {
            schema.type("integer");
            schema.format("int32");
            return schema;
        }
        if (type.is(Short.class) || type.is(short.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Character.class) || type.is(char.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Byte.class) || type.is(byte.class))
        {
            schema.type("integer");
            return schema;
        }
        return null;
    }
}

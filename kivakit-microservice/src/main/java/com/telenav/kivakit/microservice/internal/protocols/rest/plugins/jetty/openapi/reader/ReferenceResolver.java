package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.Type;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class ReferenceResolver
{
    public String reference(Type<?> typeParameter)
    {
        if (typeParameter.isPrimitive() || typeParameter.is(String.class))
        {
            return null;
        }
        return reference(typeParameter.asJavaType().getSimpleName());
    }

    public String reference(String typeName)
    {
        return "#/components/schemas/" + typeName;
    }
}

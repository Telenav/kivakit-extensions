package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.reflection.Type;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

@CodeQuality(stability = STABILITY_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class ReferenceResolver
{
    public String reference(Type<?> typeParameter)
    {
        if (typeParameter.isPrimitive() || typeParameter.is(String.class))
        {
            return null;
        }
        return reference(typeParameter.type().getSimpleName());
    }

    public String reference(String typeName)
    {
        return "#/components/schemas/" + typeName;
    }
}

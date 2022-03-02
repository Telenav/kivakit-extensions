package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.core.language.reflection.Type;

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

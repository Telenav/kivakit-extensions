package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader;

import com.telenav.kivakit.kernel.language.reflection.Type;

public class ReferenceResolver
{
    public String reference(final Type<?> typeParameter)
    {
        if (typeParameter.isPrimitive() || typeParameter.is(String.class))
        {
            return null;
        }
        return reference(typeParameter.type().getSimpleName());
    }

    public String reference(final String typeName)
    {
        return "#/components/schemas/" + typeName;
    }
}

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.language.reflection.Type;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_UNSTABLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

@CodeQuality(stability = CODE_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_PRIVATE)
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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_UNSTABLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_PRIVATE)
public class OpenApiTypeFilter implements Filter<Type<?>>
{
    @Override
    public boolean accepts(Type<?> type)
    {
        return type.isPrimitive()
                || type.is(Double.class)
                || type.is(Float.class)
                || type.is(Long.class)
                || type.is(Integer.class)
                || type.is(Short.class)
                || type.is(Character.class)
                || type.is(Byte.class)
                || type.is(String.class)
                || type.isEnum()
                || type.hasAnnotation(OpenApiIncludeType.class)
                || type.isDescendantOf(Collection.class);
    }
}

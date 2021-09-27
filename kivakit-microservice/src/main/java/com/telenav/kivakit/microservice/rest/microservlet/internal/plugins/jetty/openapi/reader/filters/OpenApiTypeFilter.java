package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader.filters;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;

import java.util.Collection;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
public class OpenApiTypeFilter implements Filter<Type<?>>
{
    @Override
    public boolean accepts(final Type<?> type)
    {
        return type.isPrimitive()
                || type.is(String.class)
                || type.isEnum()
                || type.hasAnnotation(OpenApiIncludeType.class)
                || type.isDescendantOf(Collection.class);
    }
}

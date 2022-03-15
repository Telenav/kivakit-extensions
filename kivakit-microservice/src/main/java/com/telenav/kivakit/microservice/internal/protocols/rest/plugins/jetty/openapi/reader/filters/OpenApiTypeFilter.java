package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters;

import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;

import java.util.Collection;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
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

package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.filters;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeType;

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
        return type.hasAnnotation(OpenApiIncludeType.class);
    }
}

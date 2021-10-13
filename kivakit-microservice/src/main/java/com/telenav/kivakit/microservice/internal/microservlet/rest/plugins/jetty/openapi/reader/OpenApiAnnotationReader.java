package com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
public class OpenApiAnnotationReader extends BaseComponent
{
    public <T extends Annotation> String readAnnotationValue(
            final Class<? extends MicroservletRequest> requestType,
            final String method,
            final Class<T> annotationClass,
            final Function<T, String> function)
    {
        try
        {
            final var annotation = requestType
                    .getMethod(method)
                    .getAnnotation(annotationClass);

            if (annotation != null)
            {
                var value = function.apply(annotation);
                if (!Strings.isEmpty(value))
                {
                    return value;
                }
            }
            return null;
        }
        catch (final NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return null;
        }
    }
}

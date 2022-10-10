package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class OpenApiAnnotationReader extends BaseComponent
{
    public <T extends Annotation> String readAnnotationString(
            Class<? extends MicroservletRequest> requestType,
            String method,
            Class<T> annotationClass,
            Function<T, String> function)
    {
        try
        {
            var annotation = requestType
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
        catch (NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return null;
        }
    }

    public <T extends Annotation> StringList readAnnotationStringList(
            Class<? extends MicroservletRequest> requestType,
            String method,
            Class<T> annotationClass,
            Function<T, String[]> function)
    {
        try
        {
            var annotation = requestType
                    .getMethod(method)
                    .getAnnotation(annotationClass);

            if (annotation != null)
            {
                return StringList.stringList(function.apply(annotation));
            }
            return null;
        }
        catch (NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return null;
        }
    }
}

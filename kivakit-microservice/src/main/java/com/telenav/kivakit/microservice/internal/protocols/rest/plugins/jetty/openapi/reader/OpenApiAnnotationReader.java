package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.ApiStability.API_UNSTABLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_UNSTABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE,
            type = PRIVATE)
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

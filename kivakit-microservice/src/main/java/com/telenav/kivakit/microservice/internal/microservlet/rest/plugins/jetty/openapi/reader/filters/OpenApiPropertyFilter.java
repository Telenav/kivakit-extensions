package com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.openapi.reader.filters;

import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiIncludeMemberFromSuperType;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Property filter that includes getters and fields marked with {@link OpenApiIncludeMember}
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class OpenApiPropertyFilter implements PropertyFilter
{
    private final Type<?> type;

    public OpenApiPropertyFilter(final Type<?> type)
    {
        this.type = type;
    }

    @Override
    public boolean includeAsGetter(final Method method)
    {
        return include(method);
    }

    @Override
    public boolean includeAsSetter(final Method method)
    {
        return false;
    }

    @Override
    public boolean includeField(final Field field)
    {
        return include(field);
    }

    @Override
    public String nameForField(final Field field)
    {
        return field.getName();
    }

    @Override
    public String nameForMethod(final Method method)
    {
        return method.getName();
    }

    /**
     * @param type The type to check
     * @param member The member to check
     * @return True if the member is excluded from the type
     */
    private boolean exclude(final Type<?> type, final AnnotatedElement member)
    {
        // Get any exclude annotation
        final var exclude = type.annotation(OpenApiExcludeMember.class);

        if (exclude != null)
        {
            // and for each member name excluded by the annotation,
            for (final var excludedMemberName : exclude.value())
            {
                // exclude the member if its excludedMemberName matches.
                if (excludedMemberName.equals(((Member) member).getName()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param member The member to check
     * @return True if the member is included by @{@link OpenApiIncludeMember} and not excluded by {@link
     * OpenApiExcludeMember}.
     */
    private boolean include(final AnnotatedElement member)
    {
        // For all super types of this type,
        for (final var at : type.superTypes().with(type))
        {
            // don't include if the type is excluded,
            if (exclude(at, member))
            {
                return false;
            }

            // For each @OpenApiIncludeMemberFromSuperType annotation,
            for (var annotation : at.annotations(OpenApiIncludeMemberFromSuperType.class))
            {
                // if the name matches this member,
                if (annotation.member().equals(((Member) member).getName()))
                {
                    // then include it.
                    return true;
                }
            }
        }

        // The member is included if it has an @OpenApiIncludeMember annotation.
        return member.getAnnotation(OpenApiIncludeMember.class) != null;
    }
}

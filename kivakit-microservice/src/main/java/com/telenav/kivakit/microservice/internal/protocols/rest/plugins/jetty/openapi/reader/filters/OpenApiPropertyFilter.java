package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMemberFromSuperType;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Property filter that includes getters and fields marked with {@link OpenApiIncludeMember}
 * </p>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class OpenApiPropertyFilter implements PropertyFilter
{
    private final Type<?> type;

    public OpenApiPropertyFilter(Type<?> type)
    {
        this.type = type;
    }

    @Override
    public boolean includeAsGetter(Method method)
    {
        return include(method);
    }

    @Override
    public boolean includeAsSetter(Method method)
    {
        return false;
    }

    @Override
    public boolean includeField(Field field)
    {
        return include(field);
    }

    @Override
    public String nameForField(Field field)
    {
        return field.name();
    }

    @Override
    public String nameForMethod(Method method)
    {
        return method.name();
    }

    /**
     * @param type The type to check
     * @param member The member to check
     * @return True if the member is excluded from the type
     */
    private boolean exclude(Type<?> type, Member member)
    {
        // Get any exclude annotation
        var exclude = type.annotation(OpenApiExcludeMember.class);

        if (exclude != null)
        {
            // and for each member name excluded by the annotation,
            for (var excludedMemberName : exclude.value())
            {
                // exclude the member if its excludedMemberName matches.
                if (excludedMemberName.equals(member.name()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param member The member to check
     * @return True if the member is included by @{@link OpenApiIncludeMember} and not excluded by
     * {@link OpenApiExcludeMember}.
     */
    private boolean include(Member member)
    {
        // For all super types of this type,
        for (var at : type.superTypes().with(type))
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
                if (annotation.member().equals(member.name()))
                {
                    // then include it.
                    return true;
                }
            }
        }

        // The member is included if it has an @OpenApiIncludeMember annotation.
        return member.annotation(OpenApiIncludeMember.class) != null;
    }
}

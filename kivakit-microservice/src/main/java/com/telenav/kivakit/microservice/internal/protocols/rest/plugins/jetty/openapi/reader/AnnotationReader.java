package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import io.swagger.v3.oas.models.media.Schema;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class AnnotationReader
{
    public void copyToSchema(Member member, Schema<?> schema)
    {
        var annotation = member.annotation(OpenApiIncludeMember.class);
        if (annotation != null)
        {
            if (!Strings.isEmpty(annotation.title()))
            {
                schema.title(annotation.title());
            }
            if (!Strings.isEmpty(annotation.description()))
            {
                schema.description(annotation.description());
            }
            if (!Strings.isEmpty(annotation.example()))
            {
                schema.example(annotation.example());
            }
            if (!Strings.isEmpty(annotation.type()))
            {
                schema.type(annotation.type());
            }
            if (!Strings.isEmpty(annotation.format()))
            {
                schema.format(annotation.format());
            }
            if (annotation.deprecated())
            {
                schema.deprecated(true);
            }
            if (!Strings.isEmpty(annotation.defaultValue()))
            {
                schema.setDefault(annotation.defaultValue());
            }
            if (annotation.nullable())
            {
                schema.nullable(true);
            }
            if (!Strings.isEmpty(annotation.reference()))
            {
                schema.$ref(annotation.reference());
            }
        }
    }
}

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.Member;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import io.swagger.v3.oas.models.media.Schema;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class AnnotationReader
{
    public void copyToSchema(Member member, Schema<?> schema)
    {
        var annotation = member.annotation(OpenApiIncludeMember.class);
        if (annotation != null)
        {
            if (!Strings.isNullOrBlank(annotation.title()))
            {
                schema.title(annotation.title());
            }
            if (!Strings.isNullOrBlank(annotation.description()))
            {
                schema.description(annotation.description());
            }
            if (!Strings.isNullOrBlank(annotation.example()))
            {
                schema.example(annotation.example());
            }
            if (!Strings.isNullOrBlank(annotation.type()))
            {
                schema.type(annotation.type());
            }
            if (!Strings.isNullOrBlank(annotation.format()))
            {
                schema.format(annotation.format());
            }
            if (annotation.deprecated())
            {
                schema.deprecated(true);
            }
            if (!Strings.isNullOrBlank(annotation.defaultValue()))
            {
                schema.setDefault(annotation.defaultValue());
            }
            if (annotation.nullable())
            {
                schema.nullable(true);
            }
            if (!Strings.isNullOrBlank(annotation.reference()))
            {
                schema.$ref(annotation.reference());
            }
        }
    }
}

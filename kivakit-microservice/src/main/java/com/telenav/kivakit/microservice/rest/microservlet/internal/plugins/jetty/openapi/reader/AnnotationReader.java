package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader;

import com.telenav.kivakit.kernel.language.reflection.Member;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
import io.swagger.v3.oas.models.media.Schema;

public class AnnotationReader
{
    public void copyToSchema(final Member member, final Schema schema)
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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import io.swagger.v3.oas.models.media.Schema;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

@CodeQuality(stability = STABILITY_UNSTABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class SchemaCopier
{
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void copy(Schema from, Schema<?> to)
    {
        to.set$ref(from.get$ref());
        to.setDefault(from.getDefault());
        to.deprecated(from.getDeprecated());
        to.description(from.getDescription());
        to.setEnum(from.getEnum());
        to.format(from.getFormat());
        to.name(from.getName());
        to.title(from.getTitle());
        to.type(from.getType());
        to.properties(from.getProperties());
    }

    public Schema<?> copy(Schema<?> that)
    {
        var copy = new Schema<>();
        copy(that, copy);
        return copy;
    }
}

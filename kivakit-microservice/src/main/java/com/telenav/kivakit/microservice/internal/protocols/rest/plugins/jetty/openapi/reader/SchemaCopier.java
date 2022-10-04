package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.annotations.code.ApiQuality;
import io.swagger.v3.oas.models.media.Schema;

import static com.telenav.kivakit.annotations.code.ApiStability.API_UNSTABLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

@ApiQuality(stability = API_UNSTABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE,
            type = PRIVATE)
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

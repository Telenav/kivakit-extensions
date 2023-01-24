package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections;

import com.telenav.kivakit.data.formats.yaml.YamlBlock;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.OpenApiSchemas;

import static com.telenav.kivakit.data.formats.yaml.YamlBlock.block;

public class Components
{
    private final OpenApiSchemas schemas;

    public Components(OpenApiSchemas schemas)
    {
        this.schemas = schemas;
    }

    public YamlBlock yaml()
    {
        return block("components")
            .with(schemas());
    }

    private YamlBlock schemas()
    {
        var result = block("schemas");
        for (var schema : schemas.schemas())
        {
            result = result.with(schema.yaml());
        }
        return result;
    }
}

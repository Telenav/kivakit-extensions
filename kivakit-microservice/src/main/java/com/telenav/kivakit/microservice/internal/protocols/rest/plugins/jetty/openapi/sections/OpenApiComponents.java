package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchemas;

import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;

public class OpenApiComponents
{
    private final OpenApiSchemas schemas;

    public OpenApiComponents(OpenApiSchemas schemas)
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

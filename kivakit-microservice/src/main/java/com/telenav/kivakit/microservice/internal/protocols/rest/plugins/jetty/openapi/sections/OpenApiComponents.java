package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchemas;

import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;

public class OpenApiComponents
{
    public static OpenApiComponents openApiComponents(OpenApiSchemas schemas)
    {
        return new OpenApiComponents(schemas);
    }

    private final OpenApiSchemas schemas;

    protected OpenApiComponents(OpenApiSchemas schemas)
    {
        this.schemas = schemas;
    }

    public YamlBlock yaml()
    {
        return yamlBlock("components")
            .with(schemas());
    }

    private YamlBlock schemas()
    {
        var block = yamlBlock("schemas");
        for (var schema : schemas.schemas().sorted())
        {
            block = block.with(schema.yaml());
        }
        return block;
    }
}

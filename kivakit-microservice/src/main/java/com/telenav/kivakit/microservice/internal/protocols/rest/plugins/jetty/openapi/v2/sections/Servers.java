package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections;

import com.telenav.kivakit.data.formats.yaml.YamlBlock;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.OpenApiSchemas;

public class Servers
{
    private final OpenApiSchemas schemas;

    public Servers(OpenApiSchemas schemas)
    {
        this.schemas = schemas;
    }

    public YamlBlock yaml()
    {
        return schemas.schema("servers").yaml();
    }
}


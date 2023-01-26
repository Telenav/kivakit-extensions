package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchemas;

public class OpenApiServers
{
    private final OpenApiSchemas schemas;

    public OpenApiServers(OpenApiSchemas schemas)
    {
        this.schemas = schemas;
    }

    public YamlNode yaml()
    {
        return schemas.schema("servers").yaml();
    }
}


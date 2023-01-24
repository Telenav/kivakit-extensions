package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2;

import com.telenav.kivakit.microservice.internal.yaml.YamlBlock;

public class OpenApiSchema
{
    private final YamlBlock root;

    public OpenApiSchema(YamlBlock root)
    {
        this.root = root;
    }

    public YamlBlock yaml()
    {
        return root;
    }
}

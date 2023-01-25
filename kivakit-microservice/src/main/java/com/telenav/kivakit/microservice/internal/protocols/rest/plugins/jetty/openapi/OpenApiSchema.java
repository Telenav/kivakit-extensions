package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;

public class OpenApiSchema
{
    private final YamlBlock root;

    private final String name;

    public OpenApiSchema(String name, YamlBlock root)
    {
        this.name = name;
        this.root = root;
    }

    public String name()
    {
        return name;
    }

    public YamlBlock yaml()
    {
        return root;
    }
}

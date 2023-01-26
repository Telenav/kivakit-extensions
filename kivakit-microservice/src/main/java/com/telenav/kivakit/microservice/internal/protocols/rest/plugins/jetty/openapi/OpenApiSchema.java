package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.data.formats.yaml.model.YamlNode;

public class OpenApiSchema
{
    private final YamlNode root;

    private final String name;

    public OpenApiSchema(String name, YamlNode root)
    {
        this.name = name;
        this.root = root;
    }

    public String name()
    {
        return name;
    }

    public YamlNode yaml()
    {
        return root;
    }
}

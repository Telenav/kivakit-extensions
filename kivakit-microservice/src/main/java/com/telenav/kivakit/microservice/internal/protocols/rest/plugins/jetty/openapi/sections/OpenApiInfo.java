package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;

import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlScalar.scalar;

public class OpenApiInfo extends BaseComponent
{
    public YamlBlock yaml()
    {
        var microservice = require(Microservice.class);
        return block("info")
            .with(scalar("title", microservice.name()))
            .with(scalar("description", microservice.description()))
            .with(scalar("version", microservice.version().toString()));
    }
}

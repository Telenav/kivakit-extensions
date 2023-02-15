package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;

import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

public class OpenApiInfo extends BaseComponent
{
    public static OpenApiInfo openApiInfo()
    {
        return new OpenApiInfo();
    }

    protected OpenApiInfo()
    {
    }

    public YamlBlock yaml()
    {
        var microservice = require(Microservice.class);
        return yamlBlock("info")
            .with(yamlScalar("title", microservice.name()))
            .with(yamlScalar("description", microservice.description()))
            .with(yamlScalar("version", microservice.restService().apiVersion().toString()));
    }
}

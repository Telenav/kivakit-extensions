package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiComponents;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiInfo;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiPaths;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiServers;

import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlScalar.scalar;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

public class OpenApiBuilder extends BaseComponent
{
    private final OpenApiSchemas schemas;

    public OpenApiBuilder()
    {
        var microserviceType = require(Microservice.class).getClass();
        schemas = new OpenApiSchemas()
            .add(parsePackage(this, microserviceType, "schemas"));
    }

    public YamlBlock build()
    {
        return block()
            .with(scalar("openapi", "3.0.0"))
            .with(new OpenApiInfo().yaml())
            .with(new OpenApiServers(schemas).yaml())
            .with(new OpenApiPaths().yaml())
            .with(new OpenApiComponents(schemas).yaml());
    }
}


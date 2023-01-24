package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections.Components;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections.Info;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections.Paths;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections.Servers;

import static com.telenav.kivakit.data.formats.yaml.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.YamlScalar.scalar;
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
            .with(new Info().yaml())
            .with(new Servers(schemas).yaml())
            .with(new Paths(schemas).yaml())
            .with(new Components(schemas).yaml());
    }
}


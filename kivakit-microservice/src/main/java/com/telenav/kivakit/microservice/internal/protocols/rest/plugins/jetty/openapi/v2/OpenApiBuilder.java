package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.MountedMicroservlet;
import com.telenav.kivakit.microservice.internal.yaml.Yaml;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

public class OpenApiBuilder extends BaseComponent
{
    private final OpenApiSchemas schemas;

    public OpenApiBuilder()
    {
        var microserviceType = require(Microservice.class).getClass();
        var schemasPackage = parsePackage(this, microserviceType, "schemas");
        schemas = new OpenApiSchemas(schemasPackage);
    }

    public Yaml build()
    {
        var yaml = Yaml.yaml()
            .withScalar("openapi", "3.0.0")
            .withBlock("info", info())
            .withLabel("paths")
            .indented();

        var filter = require(JettyMicroservletFilter.class);
        var paths = list(filter.microservletPaths());

        // Got through each mount path that the filter has,
        for (var path : paths.sorted())
        {
            // and add the complete YAML description for that path,
            var mounted = filter.microservlet(path);
            if (mounted != null)
            {
                yaml = yaml.withBlock("path", path(mounted));
            }
            else
            {
                problem("Unable to locate microservlet $ $", path.httpMethod(), path.path());
            }
        }

        return yaml;
    }

    private Yaml info()
    {
        var microservice = require(Microservice.class);
        return Yaml.yaml()
            .withScalar("title", microservice.name())
            .withScalar("description", microservice.description())
            .withScalar("version", microservice.version().toString());
    }

    private Yaml path(MountedMicroservlet mounted)
    {
        var microservlet = mounted.microservlet();
        var path = mounted.path();

        var requestYaml = schemas.schema(microservlet.requestType().getSimpleName()).yaml();
        var responseYaml = schemas.schema(microservlet.responseType().getSimpleName()).yaml();

        return Yaml.yaml()
            .with(path.path().toString())
            .indented()
            .with(path.httpMethod().name().toLowerCase())
            .indented()
            .withScalar("description", microservlet.description())
            .with("parameters")
            .indented()
            .with("'200'")
            .indented()
            .withBlock("request", requestYaml.asYaml())
            .withBlock("reponse", responseYaml.asYaml());
    }
}


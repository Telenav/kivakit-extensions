package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlReader.readYamlAnnotation;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchemas.openApiSchemas;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiComponents.openApiComponents;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiInfo.openApiInfo;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiPaths.openApiPaths;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

public class OpenApiBuilder extends BaseComponent
{
    public static OpenApiBuilder openApiBuilder()
    {
        return new OpenApiBuilder();
    }

    private final OpenApiSchemas schemas;

    YamlBlock yaml;

    protected OpenApiBuilder()
    {
        var microserviceType = require(Microservice.class).getClass();

        schemas = listenTo(new OpenApiSchemas())
            .add(parsePackage(this, microserviceType, "schemas"))
            .add(parsePackage(this, microserviceType, "api/schemas"))
            .addAll(restServiceSchemas());

        schemas.resolveReferences();
    }

    public YamlBlock buildYaml()
    {
        if (yaml == null)
        {
            var restServiceClass = require(RestService.class).getClass();
            var servers = readYamlAnnotation(restServiceClass, OpenApi.class, OpenApi::value);

            yaml = yamlBlock()
                .with(yamlScalar("openapi", "3.0.0"))
                .with(openApiInfo().yaml())
                .with(servers)
                .with(openApiPaths().yaml())
                .with(openApiComponents(schemas).yaml());
        }
        return yaml;
    }

    private ObjectList<OpenApiSchema> restServiceSchemas()
    {
        var filter = require(JettyMicroservletFilter.class);

        // Got through each mount path that the filter has,
        var schemas = new ObjectList<OpenApiSchema>();
        for (var mountPath : list(filter.microservletPaths()))
        {
            // and add the complete YAML description for that path,
            var microservlet = filter.microservlet(mountPath).microservlet();
            if (microservlet != null)
            {
                var requestType = microservlet.requestType();
                var responseType = microservlet.responseType();

                if (!requestType.isAssignableFrom(OpenApiRequest.class))
                {
                    var requestSchemas = openApiSchemas(this, requestType);
                    ensureNotNull(requestSchemas, "Could not extract YAML schemas from: $", requestType.getSimpleName());
                    schemas.addAll(requestSchemas);

                    var responseSchemas = openApiSchemas(this, responseType);
                    ensureNotNull(responseSchemas, "Could not extract YAML schemas from: $", responseType.getSimpleName());
                    schemas.addAll(responseSchemas);
                }
            }
        }

        var restService = require(RestService.class);
        for (var at : restService.onOpenApiSchemas())
        {
            schemas.addAll(openApiSchemas(this, at));
        }

        return schemas;
    }
}


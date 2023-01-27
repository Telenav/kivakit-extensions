package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiComponents;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiInfo;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections.OpenApiPaths;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlReader.readYamlAnnotation;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchema.schemas;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

public class OpenApiBuilder extends BaseComponent
{
    private final OpenApiSchemas schemas;

    YamlBlock yaml;

    public OpenApiBuilder()
    {
        var microserviceType = require(Microservice.class).getClass();

        schemas = listenTo(new OpenApiSchemas())
            .add(parsePackage(this, microserviceType, "schemas"))
            .add(parsePackage(this, microserviceType, "api/schemas"))
            .addAll(restServiceSchemas());
    }

    public YamlBlock buildYaml()
    {
        if (yaml == null)
        {
            var restServiceClass = require(RestService.class).getClass();
            var servers = readYamlAnnotation(restServiceClass, OpenApi.class, OpenApi::value);

            yaml = block()
                .with(scalar("openapi", "3.0.0"))
                .with(new OpenApiInfo().yaml())
                .with(servers)
                .with(new OpenApiPaths().yaml())
                .with(new OpenApiComponents(schemas).yaml());
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

                if (!requestType.isAssignableFrom(OpenApiJsonRequest.class))
                {
                    information("Adding schema for $", requestType.getSimpleName());
                    var requestSchemas = schemas(this, requestType);
                    ensureNotNull(requestSchemas, "Could not extract YAML schemas from: $", requestType.getSimpleName());
                    schemas.addAll(requestSchemas);

                    information("Adding schema for $", responseType.getSimpleName());
                    var responseSchemas = schemas(this, responseType);
                    ensureNotNull(responseSchemas, "Could not extract YAML schemas from: $", responseType.getSimpleName());
                    schemas.addAll(responseSchemas);
                }
            }
        }
        return schemas;
    }
}


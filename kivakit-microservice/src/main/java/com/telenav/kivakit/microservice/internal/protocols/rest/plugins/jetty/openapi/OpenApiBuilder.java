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
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlReader.readYamlAnnotation;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchema.schema;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;

public class OpenApiBuilder extends BaseComponent
{
    private final OpenApiSchemas schemas;

    public OpenApiBuilder()
    {
        var microserviceType = require(Microservice.class).getClass();

        schemas = new OpenApiSchemas()
            .add(parsePackage(this, microserviceType, "schemas"))
            .add(parsePackage(this, microserviceType, "api/schemas"))
            .addAll(restServiceSchemas());
    }

    public YamlBlock buildYaml()
    {
        var restServiceClass = require(RestService.class).getClass();
        var servers = readYamlAnnotation(restServiceClass, OpenApiType.class, OpenApiType::value);

        return block()
            .with(scalar("openapi", "3.0.0"))
            .with(new OpenApiInfo().yaml())
            .with(servers)
            .with(new OpenApiPaths().yaml())
            .with(new OpenApiComponents(schemas).yaml());
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
                    var requestSchema = schema(requestType);
                    ensureNotNull(requestSchema, "Could not extract YAML schema from: $", requestType.getSimpleName());
                    schemas.add(requestSchema);

                    information("Adding schema for $", responseType.getSimpleName());
                    var responseSchema = schema(responseType);
                    ensureNotNull(responseSchema, "Could not extract YAML schema from: $", responseType.getSimpleName());
                    schemas.add(schema(responseType));
                }
            }
        }
        return schemas;
    }
}


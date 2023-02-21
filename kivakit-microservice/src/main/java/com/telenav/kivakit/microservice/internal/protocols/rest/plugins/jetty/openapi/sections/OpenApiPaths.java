package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlScalar;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.MountedMicroservlet;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletError;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

public class OpenApiPaths extends BaseComponent
{
    public static OpenApiPaths openApiPaths()
    {
        return new OpenApiPaths();
    }

    protected OpenApiPaths()
    {
    }

    public YamlNode yaml()
    {
        var paths = yamlBlock("paths");

        var filter = require(JettyMicroservletFilter.class);
        var mountPaths = list(filter.microservletPaths());

        // Got through each mount path that the filter has,
        for (var mountPath : mountPaths.sorted())
        {
            // and add the complete YAML description for that path,
            var mounted = filter.microservlet(mountPath);
            if (mounted != null)
            {
                if (mounted.microservlet().requestType() != OpenApiRequest.class)
                {
                    paths = paths.with(path(mounted));
                }
            }
            else
            {
                problem("Unable to locate microservlet $ $", mountPath.httpMethod(), mountPath.path());
            }
        }

        return paths;
    }

    private YamlBlock path(MountedMicroservlet mounted)
    {
        var microservlet = mounted.microservlet();
        var path = mounted.path();
        var method = path.httpMethod().name().toLowerCase();

        return yamlBlock(path.path().toString())
            .with(yamlBlock(method)
                .with(yamlScalar("description", microservlet.description()))
                .with(requestBody(microservlet.requestType()))
                .with(responses(microservlet.responseType())));
    }

    private YamlScalar reference(Class<?> type)
    {
        return yamlScalar("$ref", "#/components/schemas/" + type.getSimpleName());
    }

    private YamlNode requestBody(Class<?> requestType)
    {
        return yamlBlock("requestBody")
            .with(yamlBlock("content")
                .with(yamlBlock("application/json")
                    .with(yamlBlock("schema")
                        .with(reference(requestType)))));
    }

    private YamlNode responses(Class<?> responseType)
    {
        return yamlBlock("responses")
            .with(yamlBlock("'200'")
                .with(yamlScalar("description", "Response object"))
                .with(yamlBlock("content")
                    .with(yamlBlock("application/json")
                        .with(yamlBlock("schema")
                            .with(reference(responseType))))))
            .with(yamlBlock("'400,405,500'")
                .with(yamlScalar("description", "Error description"))
                .with(yamlBlock("content")
                    .with(yamlBlock("application/json")
                        .with(yamlBlock("schema")
                            .with(reference(MicroservletError.class))))));
    }
}

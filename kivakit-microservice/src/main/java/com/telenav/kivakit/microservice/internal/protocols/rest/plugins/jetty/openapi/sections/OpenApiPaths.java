package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.sections;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.MountedMicroservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletError;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;

public class OpenApiPaths extends BaseComponent
{
    public YamlNode yaml()
    {
        var paths = block("paths");

        var filter = require(JettyMicroservletFilter.class);
        var mountPaths = list(filter.microservletPaths());

        // Got through each mount path that the filter has,
        for (var mountPath : mountPaths.sorted())
        {
            // and add the complete YAML description for that path,
            var mounted = filter.microservlet(mountPath);
            if (mounted != null)
            {
                paths = paths.with(path(mounted));
            }
            else
            {
                problem("Unable to locate microservlet $ $", mountPath.httpMethod(), mountPath.path());
            }
        }

        return paths;
    }

    private YamlBlock content(Class<?> type)
    {
        var reference = scalar("$ref", "#/components/schemas/" + type.getSimpleName());

        var applicationJson = block("application/json")
            .with(reference);

        return block("content")
            .with(applicationJson);
    }

    private YamlBlock path(MountedMicroservlet mounted)
    {
        var microservlet = mounted.microservlet();
        var path = mounted.path();
        var method = path.httpMethod().name().toLowerCase();

        return block(path.path().toString())
            .with(block(method)
                .with(scalar("description", microservlet.description()))
                .with(requestBody(microservlet.requestType()))
                .with(responses(microservlet.responseType())));
    }

    private YamlNode requestBody(Class<?> requestType)
    {
        return block("requestBody")
            .with(content(requestType));
    }

    private YamlNode responses(Class<?> responseType)
    {
        return block("responses")
            .with(block("'200'")
                .with(content(responseType)))
            .with(block("'500'")
                .with(content(MicroservletError.class)));
    }
}

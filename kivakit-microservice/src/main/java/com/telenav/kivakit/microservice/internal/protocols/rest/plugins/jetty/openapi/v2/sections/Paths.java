package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2.sections;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.data.formats.yaml.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.YamlNode;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.MountedMicroservlet;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.data.formats.yaml.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.YamlScalar.scalar;

/**
 * <pre>
 *
 *     post:
 *       summary: Creates a user.
 *       requestBody:
 *         required: true
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 username:
 *                   type: string
 *
 *       responses:
 *         '200':
 *           description: OK
 *           content:
 *             application/json:
 *               schema:
 *                 $ref: '#/components/schemas/User'
 * </pre>
 */
public class Paths extends BaseComponent
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
        var reference = scalar("$ref", "'#/components/schemas/" + type.getSimpleName() + "'");

        var applicationJson = block("application/json")
            .with(reference);

        return block("content")
            .with(applicationJson);
    }

    private YamlBlock path(MountedMicroservlet mounted)
    {
        var microservlet = mounted.microservlet();
        var path = mounted.path();
        var method = path.httpMethod().name();

        return block(path.path().toString())
            .with(block(method)
                .with(scalar("description", microservlet.description()))
                .with(requestBody(microservlet.requestType()))
                .with(responses(microservlet.responseType())));
    }

    private YamlNode requestBody(Class<?> requestType)
    {
        return block("requestBody")
            .with(scalar("required", true))
            .with(content(requestType));
    }

    private YamlNode responses(Class<?> responseType)
    {
        return block("responses")
            .with(block("'200'")
                .with(content(responseType)))
            .with(block("'500'")
                .with(content(MessageList.class)));
    }
}

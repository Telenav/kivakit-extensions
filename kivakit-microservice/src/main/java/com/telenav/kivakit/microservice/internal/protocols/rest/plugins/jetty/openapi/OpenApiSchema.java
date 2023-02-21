package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlNodeContainer;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;
import com.telenav.kivakit.resource.resources.StringResource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.yamlLiteral;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlReader.yamlReader;

public class OpenApiSchema implements Comparable<OpenApiSchema>
{
    public static OpenApiSchema openApiSchema(Listener listener, String name, YamlNode node)
    {
        return new OpenApiSchema(name, node);
    }

    public static OpenApiSchema openApiSchema(Listener listener, String name, Class<?> type)
    {
        listener.trace("Reading schema $", type.getSimpleName());
        ensureNotNull(name);
        var annotation = ensureNotNull(type).getAnnotation(OpenApi.class);
        if (annotation != null)
        {
            var text = annotation.value();
            var node = yamlReader().read(new StringResource(text));
            if (node instanceof YamlNodeContainer elements)
            {
                var object = yamlLiteral("type", "object");
                if (!elements.elements().contains(object))
                {
                    node = elements.prepending(object);
                }
            }

            return new OpenApiSchema(name, node);
        }

        return null;
    }

    private final YamlNode root;

    private final String name;

    protected OpenApiSchema(String name, YamlNode root)
    {
        this.name = ensureNotNull(name);
        this.root = yamlBlock(name)
            .with(ensureNotNull(root));
    }

    @Override
    public int compareTo(@NotNull OpenApiSchema that)
    {
        return name.compareTo(that.name);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof OpenApiSchema that)
        {
            return this.name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    public String name()
    {
        return name;
    }

    public YamlNode yaml()
    {
        return root;
    }
}

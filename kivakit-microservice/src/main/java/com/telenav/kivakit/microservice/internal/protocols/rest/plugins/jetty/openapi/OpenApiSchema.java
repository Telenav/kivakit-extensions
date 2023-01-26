package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.reader.YamlReader;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;
import com.telenav.kivakit.resource.resources.StringResource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.literal;

public class OpenApiSchema implements Comparable<OpenApiSchema>
{
    public static OpenApiSchema schema(Class<?> type)
    {
        return schema(type.getSimpleName(), type);
    }

    public static OpenApiSchema schema(String name, Class<?> type)
    {
        ensureNotNull(name);
        var annotation = ensureNotNull(type).getAnnotation(OpenApiType.class);
        if (annotation != null)
        {
            var text = annotation.value();
            var node = new YamlReader().read(new StringResource(text));
            if (node instanceof YamlBlock block)
            {
                node = block.prepending(literal("type", "object"));
            }
            if (node instanceof YamlArray array)
            {
                node = array.prepending(literal("type", "object"));
            }

            return new OpenApiSchema(name, node);
        }
        return null;
    }

    public static OpenApiSchema schema(String name, YamlNode node)
    {
        return new OpenApiSchema(name, node);
    }

    private final YamlNode root;

    private final String name;

    private OpenApiSchema(String name, YamlNode root)
    {
        this.name = ensureNotNull(name);
        this.root = block(name)
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

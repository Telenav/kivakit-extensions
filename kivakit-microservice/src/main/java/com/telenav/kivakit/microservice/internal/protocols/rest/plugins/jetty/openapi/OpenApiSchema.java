package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlNodeContainer;
import com.telenav.kivakit.data.formats.yaml.reader.YamlReader;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;
import com.telenav.kivakit.resource.resources.StringResource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.literal;

public class OpenApiSchema implements Comparable<OpenApiSchema>
{
    public static OpenApiSchema schema(Listener listener, String name, YamlNode node)
    {
        return new OpenApiSchema(name, node);
    }

    public static ObjectList<OpenApiSchema> schemas(Listener listener, Class<?> type)
    {
        return schemas(listener, type, new HashSet<>());
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

    private static OpenApiSchema schema(Listener listener, String name, Class<?> type)
    {
        listener.trace("Reading schema $", type.getSimpleName());
        ensureNotNull(name);
        var annotation = ensureNotNull(type).getAnnotation(OpenApi.class);
        if (annotation != null)
        {
            var text = annotation.value();
            var node = new YamlReader().read(new StringResource(text));
            if (node instanceof YamlNodeContainer elements)
            {
                var object = literal("type", "object");
                if (!elements.elements().contains(object))
                {
                    node = elements.prepending(object);
                }
            }

            return new OpenApiSchema(name, node);
        }

        return null;
    }

    private static ObjectList<OpenApiSchema> schemas(Listener listener, Class<?> type, Set<Class<?>> visited)
    {
        if (!visited.contains(type))
        {
            visited.add(type);
            var schemas = new ObjectList<OpenApiSchema>();
            schemas.addIfNotNull(schema(listener, type.getSimpleName(), type));
            for (var field : typeForClass(type).allFields())
            {
                var fieldType = field.type();
                if (fieldType.hasAnnotation(OpenApi.class))
                {
                    schemas.add(schema(listener, fieldType.simpleName(), fieldType.asJavaType()));
                    schemas.addAll(schemas(listener, fieldType.asJavaType(), visited));
                }
                for (var typeParameter : field.genericTypeParameters())
                {
                    if (typeParameter.hasAnnotation(OpenApi.class))
                    {
                        schemas.add(schema(listener, typeParameter.simpleName(), typeParameter.asJavaType()));
                        schemas.addAll(schemas(listener, typeParameter.asJavaType(), visited));
                    }
                }
            }
            return schemas;
        }
        return list();
    }
}

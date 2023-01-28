package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.data.formats.yaml.YamlTreeWalker;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlLiteral;
import com.telenav.kivakit.data.formats.yaml.reader.YamlReader;
import com.telenav.kivakit.microservice.microservlet.MicroservletError;
import com.telenav.kivakit.resource.ResourceFolder;

import java.util.Collection;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.string.CaseFormat.isCapitalized;
import static com.telenav.kivakit.resource.Extension.YAML;
import static com.telenav.kivakit.resource.Extension.YML;

public class OpenApiSchemas extends BaseComponent
{
    private final StringMap<OpenApiSchema> nameToSchema = new StringMap<>();

    public OpenApiSchemas()
    {
        addAll(OpenApiSchema.schemas(this, MicroservletError.class));
    }

    public OpenApiSchemas add(Class<?> type)
    {
        return addAll(OpenApiSchema.schemas(this, type));
    }

    public OpenApiSchemas add(ResourceFolder<?> folder)
    {
        // For each YAML file in the schema folder,
        for (var resource : folder.resources(it -> it.hasExtension(YML) || it.hasExtension(YAML)))
        {
            // parse it as a YAML block
            var block = new YamlReader().read(resource);
            var name = resource.fileName().withoutExtension().name();
            add(OpenApiSchema.schema(this, name, block));
        }
        return this;
    }

    public OpenApiSchemas add(OpenApiSchema schema)
    {
        ensureNotNull(schema, "Null schema");
        ensureNotNull(schema.yaml(), "Schema $ has no yaml", schema.name());

        if (schema.yaml().isNamed())
        {
            nameToSchema.put(schema.name(), schema);
        }
        else
        {
            fail("Unnamed schema: $", schema.yaml());
        }
        return this;
    }

    public OpenApiSchemas addAll(Collection<OpenApiSchema> schemas)
    {
        schemas.forEach(this::add);
        return this;
    }

    public OpenApiSchemas resolveReferences()
    {
        schemas().forEach(this::resolveSchema);
        return this;
    }

    public OpenApiSchema schema(Class<?> type)
    {
        var name = type.getSimpleName();
        var schema = schema(name);
        if (schema == null)
        {
            add(type);
        }
        return schema(name);
    }

    public OpenApiSchema schema(String name)
    {
        return nameToSchema.get(name);
    }

    public ObjectList<OpenApiSchema> schemas()
    {
        return ObjectList.list(nameToSchema.values());
    }

    private void resolveSchema(OpenApiSchema schema)
    {
        // Walk the YAML tree,
        new YamlTreeWalker(schema.yaml()).walk(node ->
        {
            // and if we find a literal,
            if (node instanceof YamlBlock block)
            {
                // that's declaring a type,
                var type = block.get("type");
                if (type instanceof YamlLiteral literal)
                {
                    if (isCapitalized(literal.value()))
                    {
                        block.addReference(literal.value());
                    }
                }
            }
        });
    }
}

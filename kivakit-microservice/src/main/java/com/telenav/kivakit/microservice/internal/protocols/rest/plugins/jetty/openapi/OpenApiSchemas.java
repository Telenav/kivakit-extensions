package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.data.formats.yaml.YamlTreeWalker;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlLiteral;
import com.telenav.kivakit.microservice.microservlet.MicroservletError;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;
import com.telenav.kivakit.resource.ResourceFolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.string.CaseFormat.isCapitalized;
import static com.telenav.kivakit.core.string.CaseFormat.isUppercase;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlReader.yamlReader;
import static com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiSchema.openApiSchema;
import static com.telenav.kivakit.resource.Extension.YAML;
import static com.telenav.kivakit.resource.Extension.YML;

public class OpenApiSchemas extends BaseComponent
{
    public static ObjectList<OpenApiSchema> openApiSchemas(Listener listener, Class<?> type)
    {
        return openApiSchemas(listener, type, new HashSet<>());
    }

    private final StringMap<OpenApiSchema> nameToSchema = new StringMap<>();

    protected OpenApiSchemas()
    {
        addAll(openApiSchemas(this, MicroservletError.class));
    }

    public OpenApiSchemas add(Class<?> type)
    {
        return addAll(openApiSchemas(this, type));
    }

    public OpenApiSchemas add(ResourceFolder<?> folder)
    {
        // For each YAML file in the schema folder,
        for (var resource : folder.resources(it -> it.hasExtension(YML) || it.hasExtension(YAML)))
        {
            // parse it as a YAML block
            var block = yamlReader().read(resource);
            var name = resource.fileName().withoutExtension().name();
            add(openApiSchema(this, name, block));
        }
        return this;
    }

    public OpenApiSchemas add(OpenApiSchema schema)
    {
        if (nameToSchema.get(schema.name()) == null)
        {
            ensureNotNull(schema, "Null schema");
            ensureNotNull(schema.yaml(), "Schema $ has no yaml", schema.name());

            if (schema.yaml().isNamed())
            {
                information("Adding schema $", schema.name());
                nameToSchema.put(schema.name(), schema);
            }
            else
            {
                fail("Unnamed schema: $", schema.yaml());
            }
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

    private static ObjectList<OpenApiSchema> openApiSchemas(Listener listener, Class<?> type, Set<Class<?>> visited)
    {
        if (!visited.contains(type))
        {
            visited.add(type);
            var schemas = new ObjectList<OpenApiSchema>();
            schemas.addIfNotNull(openApiSchema(listener, type.getSimpleName(), type));
            for (var field : typeForClass(type).allFields())
            {
                var fieldType = field.type();
                if (fieldType.hasAnnotation(OpenApi.class))
                {
                    schemas.add(openApiSchema(listener, fieldType.simpleName(), fieldType.asJavaType()));
                    schemas.addAll(openApiSchemas(listener, fieldType.asJavaType(), visited));
                }
                for (var typeParameter : field.genericTypeParameters())
                {
                    if (typeParameter.hasAnnotation(OpenApi.class))
                    {
                        schemas.add(openApiSchema(listener, typeParameter.simpleName(), typeParameter.asJavaType()));
                        schemas.addAll(openApiSchemas(listener, typeParameter.asJavaType(), visited));
                    }
                }
            }
            return schemas;
        }
        return list();
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
                    var value = literal.value();
                    if (isCapitalized(value) && !isUppercase(value))
                    {
                        block.addReference(value);
                    }
                }
            }
        });
    }
}

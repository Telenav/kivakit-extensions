package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.data.formats.yaml.reader.YamlReader;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.resources.StringResource;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.resource.Extension.YAML;
import static com.telenav.kivakit.resource.Extension.YML;

public class OpenApiSchemas extends BaseComponent
{
    private final StringMap<OpenApiSchema> schemas = new StringMap<>();

    public OpenApiSchemas()
    {
        add("servers", require(RestService.class).getClass());
    }

    public OpenApiSchemas add(Class<?> type)
    {
        return add(type.getSimpleName(), type);
    }

    public OpenApiSchemas add(ResourceFolder<?> folder)
    {
        // For each YAML file in the schema folder,
        for (var resource : folder.resources(it -> it.hasExtension(YML) || it.hasExtension(YAML)))
        {
            // parse it as a YAML block
            var block = new YamlReader().read(resource);
            var name = resource.fileName().name();
            add(new OpenApiSchema(name, block));
        }
        return this;
    }

    public OpenApiSchemas add(OpenApiSchema schema)
    {
        schemas.put(schema.name(), schema);
        return this;
    }

    public OpenApiSchema schema(Class<?> type)
    {
        var name = type.getSimpleName();
        var schema = schema(name);
        if (schema == null)
        {
            add(name, type);
        }
        return schema(name);
    }

    public OpenApiSchema schema(String name)
    {
        return schemas.get(name);
    }

    public ObjectList<OpenApiSchema> schemas()
    {
        return list(schemas.values());
    }

    private OpenApiSchemas add(String name, Class<?> type)
    {
        var annotation = type.getAnnotation(OpenApiType.class);
        if (annotation != null)
        {
            var text = annotation.value();
            var block = new YamlReader().read(new StringResource(text));
            add(new OpenApiSchema(name, block));
        }
        return this;
    }
}

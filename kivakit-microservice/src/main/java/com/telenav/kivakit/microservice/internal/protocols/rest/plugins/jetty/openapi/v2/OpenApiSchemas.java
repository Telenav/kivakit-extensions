package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.v2;

import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.microservice.internal.yaml.reader.YamlReader;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.resources.StringResource;

import static com.telenav.kivakit.resource.Extension.YAML;
import static com.telenav.kivakit.resource.Extension.YML;

public class OpenApiSchemas
{
    private final StringMap<OpenApiSchema> schemas = new StringMap<>();

    public OpenApiSchemas(ResourceFolder<?> folder)
    {
        // For each YAML file in the schema folder,
        for (var resource : folder.resources(it -> it.hasExtension(YML) || it.hasExtension(YAML)))
        {
            // parse it as a YAML block
            var block = new YamlReader().read(resource);
            schemas.put(resource.fileName().name(), new OpenApiSchema(block));
        }
    }

    public OpenApiSchemas add(Class<?> type)
    {
        var annotation = type.getAnnotation(OpenApiType.class);
        if (annotation != null)
        {
            var text = annotation.value();
            var block = new YamlReader().read(new StringResource(text));
            schemas.put(type.getSimpleName(), new OpenApiSchema(block));
        }
        return this;
    }

    public OpenApiSchema schema(String name)
    {
        return schemas.get(name);
    }
}

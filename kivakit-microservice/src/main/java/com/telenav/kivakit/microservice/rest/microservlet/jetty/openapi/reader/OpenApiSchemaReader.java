package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.reflection.Type;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Reads OpenAPI Schema models from the set of model classes added to this reader with {@link #add(Class)}
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("rawtypes") public class OpenApiSchemaReader extends BaseComponent
{

    /** Models for which to make schemas */
    private final Set<Class<?>> models = new HashSet<>();

    public OpenApiSchemaReader()
    {
        register(this);
    }

    /**
     * Adds the given model to this reader. Models found by {@link OpenApiPathReader} are added via this method.
     */
    public OpenApiSchemaReader add(final Class<?> model)
    {
        models.add(ensureNotNull(model));
        return this;
    }

    /**
     * @return A {@link Components} object containing all schemas added to this reader via {@link #add(Class)}.
     */
    public Components read()
    {
        var components = new Components();

        var schemas = new HashMap<String, Schema>();
        for (var model : models)
        {
            read(schemas, model);
        }

        components.schemas(schemas);

        return components;
    }

    public Schema<?> read(final Map<String, Schema> schemas,
                          final Class<?> model)
    {
        var schema = new Schema<>();
        var type = Type.of(model);

        if (isPrimitive(schema, type))
        {
            return schema;
        }

        if (type.is(Array.class))
        {

        }

        schema.type("object");
        for (var property : type.properties(new OpenApiPropertyFilter()))
        {
            var name = property.type().getSimpleName();
            schemas.put()
        }

        return schema;
    }

    private boolean isPrimitive(final Schema<Object> schema, final Type<Object> type)
    {
        if (type.is(Double.class))
        {
            schema.type("number");
            schema.format("double");
            return true;
        }
        if (type.is(Float.class))
        {
            schema.type("number");
            schema.format("float");
            return true;
        }
        if (type.is(Long.class))
        {
            schema.type("integer");
            schema.format("int64");
            return true;
        }
        if (type.is(Integer.class))
        {
            schema.type("integer");
            schema.format("int32");
            return true;
        }
        if (type.is(Short.class))
        {
            schema.type("integer");
            return true;
        }
        if (type.is(Character.class))
        {
            schema.type("integer");
            return true;
        }
        if (type.is(Byte.class))
        {
            schema.type("integer");
            return true;
        }
        return false;
    }
}

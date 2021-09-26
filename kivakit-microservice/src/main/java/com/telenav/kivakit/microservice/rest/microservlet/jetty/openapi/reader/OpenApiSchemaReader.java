package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeType;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.filters.OpenApiPropertyFilter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.filters.OpenApiTypeFilter;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Reads OpenAPI Schema models from annotated Java types.
 * </p>
 *
 * <p>
 * * The {@link #read()} method reads all schemas rom the set of model classes added to this reader with {@link
 * #add(Type)}. The {@link #readSchema(Type)} method reads the schema for a single type.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see OpenApiIncludeType
 * @see OpenApiExcludeMember
 * @see OpenApiIncludeMember
 */
@SuppressWarnings("rawtypes") public class OpenApiSchemaReader extends BaseComponent
{
    /** Models for which to make schemas */
    private final Set<Type<?>> models = new HashSet<>();

    public OpenApiSchemaReader()
    {
        register(this);
    }

    /**
     * Adds the given model to this reader. Models found by {@link OpenApiPathReader} are added via this method.
     */
    public OpenApiSchemaReader add(final Type<?> model)
    {
        ensureFalse(model.is(Class.class));
        models.add(ensureNotNull(model));
        return this;
    }

    /**
     * @return A map from schema name to {@link Schema} for all schemas found recursively in all models added to this
     * reader via {@link #add(Type)}.
     */
    public Map<String, Schema> read()
    {
        final var schemas = new HashMap<String, Schema>();

        models.forEach(model ->
        {
            final var schema = readSchema(model);
            if (schema != null)
            {
                schemas.put(model.simpleName(), schema);
            }
        });

        return schemas;
    }

    /**
     * Reads the schema of a model type. Supports primitive types, arrays and objects.
     *
     * @param type The model type
     * @return The OpenApi {@link Schema} or null if the type is ignored
     */
    public Schema<?> readSchema(final Type<?> type)
    {
        if (new OpenApiTypeFilter().accepts(type))
        {
            var schema = readPrimitive(type);
            if (schema == null)
            {
                return readObject(type);
            }
        }
        return null;
    }

    /**
     * @param type The type
     * @return An object {@link Schema} if the given type is an array, null otherwise
     */
    private Schema readArray(final Type<?> type, Type<?> typeParameter)
    {
        if (type.is(Array.class))
        {
            final var schema = new ArraySchema();
            schema.type("array");
            schema.items(readSchema(type.arrayElementType()));
            return schema;
        }
        if (type.isDescendantOf(Collection.class))
        {
            final var schema = new ArraySchema();
            schema.type("array");
            final var typeParameterSchema = readSchema(typeParameter);
            typeParameterSchema.$ref(reference(typeParameter));
            schema.items(typeParameterSchema);
            return schema;
        }
        return null;
    }

    /**
     * @param type The type to read from
     * @return The object {@link Schema}.
     */
    private Schema<?> readObject(final Type<?> type)
    {
        final var annotation = type.annotation(OpenApiIncludeType.class);
        if (annotation != null)
        {
            final var schema = new Schema<>();

            // Add object attributes,
            schema.type("object");
            schema.name(type.simpleName());
            schema.description(annotation.description());
            schema.setDeprecated(annotation.deprecated() ? true : null);
            schema.setTitle(annotation.title());

            // and if the type is an enum,
            if (type.isEnum())
            {
                // add the enum values.
                schema.setEnum(List.of(type.enumValues()));
            }

            // For each property
            final var properties = new HashMap<String, Schema>();
            final var required = new ArrayList<String>();
            for (final var property : type.properties(new OpenApiPropertyFilter(type)))
            {
                var field = property.field();
                if (field.type().isDescendantOf(Collection.class) || field.type().isArray())
                {
                    final var parameters = field.genericTypeParameters();
                    if (parameters.size() == 1)
                    {
                        properties.put(property.name(), readArray(field.type(), parameters.get(0)));
                    }
                }
                else
                {
                    // add its schema by name.
                    final Schema<?> propertySchema = readSchema(property.type());
                    if (propertySchema != null)
                    {
                        final var includeAnnotation = type.annotation(OpenApiIncludeMember.class);
                        if (includeAnnotation != null)
                        {
                            if (propertySchema.getType().equals("object"))
                            {
                                propertySchema.$ref(reference(property.type()));
                            }

                            propertySchema.name(property.name());
                            propertySchema.setDefault(includeAnnotation.defaultValue());
                            propertySchema.nullable(includeAnnotation.nullable());
                            propertySchema.description(includeAnnotation.description());
                            propertySchema.example(includeAnnotation.example());
                            propertySchema.deprecated(includeAnnotation.deprecated() ? true : null);

                            if (includeAnnotation.required())
                            {
                                required.add(property.name());
                            }

                            final var reference = includeAnnotation.reference();
                            if (reference != null)
                            {
                                propertySchema.$ref(reference);
                            }

                            properties.put(property.name(), propertySchema);
                        }
                    }
                }
            }
            schema.required(required);
            schema.properties(properties);

            return schema;
        }
        else
        {
            warning("Type is missing @OpenApiIncludeType annotation: $", type);
        }

        return null;
    }

    /**
     * Populates the given schema object with a type and format if the type parameter is a primitive value class.
     *
     * @param type The type of object
     * @return The schema for the object if it is primitive or null if it is not
     */
    private Schema readPrimitive(final Type<?> type)
    {
        final var schema = new Schema<>();
        if (type.is(Double.class))
        {
            schema.type("number");
            schema.format("double");
            return schema;
        }
        if (type.is(Float.class))
        {
            schema.type("number");
            schema.format("float");
            return schema;
        }
        if (type.is(Long.class))
        {
            schema.type("integer");
            schema.format("int64");
            return schema;
        }
        if (type.is(Integer.class))
        {
            schema.type("integer");
            schema.format("int32");
            return schema;
        }
        if (type.is(Short.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Character.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Byte.class))
        {
            schema.type("integer");
            return schema;
        }
        return null;
    }

    private String reference(final Type<?> typeParameter)
    {
        return "#/components/schemas/" + typeParameter.simpleName();
    }
}

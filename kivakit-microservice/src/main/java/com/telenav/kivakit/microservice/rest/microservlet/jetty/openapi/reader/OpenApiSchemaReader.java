package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.reflection.Member;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeType;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.filters.OpenApiPropertyFilter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader.filters.OpenApiTypeFilter;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletErrors;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * #addModelToRead(Type)}. The {@link #readSchema(Type)} method reads the schema for a single type.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see OpenApiIncludeType
 * @see OpenApiExcludeMember
 * @see OpenApiIncludeMember
 */
@SuppressWarnings("rawtypes") public class OpenApiSchemaReader extends BaseComponent
{
    public static String reference(final Type<?> typeParameter)
    {
        return "#/components/schemas/" + typeParameter.type().getSimpleName();
    }

    /** Models for which to make schemas */
    private final ObjectSet<Type<?>> modelsToRead = new ObjectSet<>();

    /** The schemas that we've resolved */
    private final Map<String, Schema> resolvedSchemas = new HashMap<>();

    public OpenApiSchemaReader()
    {
        register(this);
    }

    /**
     * Adds the given model to this reader. Models found by {@link OpenApiPathReader} are added via this method.
     */
    public OpenApiSchemaReader addModelToRead(final Type<?> model)
    {
        ensureFalse(model.is(Class.class));
        modelsToRead.add(ensureNotNull(model));
        return this;
    }

    /**
     * @return A map from schema name to {@link Schema} for all schemas found recursively in all models added to this
     * reader via {@link #addModelToRead(Type)}.
     */
    public Map<String, Schema> read()
    {
        // Resolve models into schemas,
        modelsToRead.forEach(this::readSchema);

        // and return the resolved schemas.
        return resolvedSchemas;
    }

    /**
     * Reads the schema of a model type. Supports primitive types, arrays and objects.
     *
     * @param type The model type
     * @return The OpenApi {@link Schema} or null if the type is ignored
     */
    public Schema<?> readSchema(final Type<?> type)
    {
        // If we haven't already resolved the schema,
        final String name = type.type().getSimpleName();
        var resolved = resolvedSchemas.get(name);
        if (resolved == null)
        {
            // and the type is included,
            if (new OpenApiTypeFilter().accepts(type))
            {
                // then resolve the schema,
                resolved = readObject(type);

                // and add it to the resolved schema map.
                if (resolved != null)
                {
                    if (!type.isPrimitive() && !type.is(String.class))
                    {
                        resolvedSchemas.put(name, resolved);
                    }
                }
            }
        }

        return resolved;
    }

    /**
     * @return The {@link Schema} for {@link MicroservletErrors}s.
     */
    public Schema<?> schemaError()
    {
        return readSchema(Type.forClass(MicroservletErrors.class));
    }

    private Schema copy(Schema that)
    {
        var copy = new Schema<>();
        copy(that, copy);
        return copy;
    }

    @SuppressWarnings("unchecked")
    private void copy(Schema from, Schema to)
    {
        to.set$ref(from.get$ref());
        to.setDefault(from.getDefault());
        to.deprecated(from.getDeprecated());
        to.description(from.getDescription());
        to.setEnum(from.getEnum());
        to.format(from.getFormat());
        to.name(from.getName());
        to.title(from.getTitle());
        to.type(from.getType());
        to.properties(from.getProperties());
    }

    /**
     * @param member The member
     * @return An object {@link Schema} if the given type is an array, null otherwise
     */
    private Schema readArray(final Member member, Type<?> typeParameter)
    {
        Schema elementTypeSchema = null;
        final Type<?> type = member.type();
        if (type.is(Array.class))
        {
            elementTypeSchema = readSchema(type.arrayElementType());
        }
        if (type.isDescendantOf(Collection.class))
        {
            final var parameters = member.genericTypeParameters();
            if (parameters.size() == 1)
            {
                elementTypeSchema = copy(readSchema(parameters.get(0)));
            }
            else
            {
                problem("Member must have exactly one type parameters: $", member);
            }
        }

        if (elementTypeSchema != null)
        {
            final var schema = new ArraySchema();
            schema.type("array");
            return schema.items(new Schema().name(typeParameter.name())
                    .type(elementTypeSchema.getType())
                    .$ref(reference(typeParameter)));
        }
        return null;
    }

    /**
     * @param type The type to read from
     * @return The object {@link Schema}.
     */
    private Schema<?> readObject(final Type<?> type)
    {
        var primitiveSchema = readPrimitive(type);
        if (primitiveSchema != null)
        {
            return primitiveSchema;
        }

        final var annotation = type.annotation(OpenApiIncludeType.class);
        if (annotation != null)
        {
            final var schema = new Schema<>();

            // Add object attributes,
            schema.type("object");
            schema.name(type.type().getSimpleName());
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
                // get the member,
                var member = property.member();

                // and if it's a collection or array,
                if (member.type().isDescendantOf(Collection.class) || member.type().isArray())
                {
                    // get the generic type parameter,
                    final var parameters = member.genericTypeParameters();
                    if (parameters.size() == 1)
                    {
                        // and add the member as an array of the type parameter type.
                        properties.put(property.name(), readArray(member, parameters.get(0)));
                    }
                }
                else
                {
                    // otherwise, read the property's schema,
                    var propertyTypeSchema = readSchema(property.type());
                    var propertySchema = new Schema();
                    propertySchema.name(property.name());

                    if (propertyTypeSchema != null)
                    {
                        propertySchema.type(propertyTypeSchema.getType());
                        if (propertyTypeSchema.getType().equals("object"))
                        {
                            propertySchema.$ref(reference(property.type()));
                        }
                        propertySchema.format(propertyTypeSchema.getFormat());
                    }

                    final var includeAnnotation = member.annotation(OpenApiIncludeMember.class);
                    if (includeAnnotation != null)
                    {
                        propertySchema.setDefault(includeAnnotation.defaultValue());
                        propertySchema.nullable(includeAnnotation.nullable() ? true : null);
                        propertySchema.description(includeAnnotation.description());
                        propertySchema.example(includeAnnotation.example());
                        propertySchema.deprecated(includeAnnotation.deprecated() ? true : null);

                        if (includeAnnotation.required())
                        {
                            required.add(property.name());
                        }

                        final var reference = includeAnnotation.reference();
                        if (!Strings.isEmpty(reference))
                        {
                            propertySchema.$ref(reference);
                        }

                        properties.put(property.name(), propertySchema);
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
        if (type.is(String.class))
        {
            schema.type("string");
            return schema;
        }
        if (type.is(Double.class) || type.is(double.class))
        {
            schema.type("number");
            schema.format("double");
            return schema;
        }
        if (type.is(Float.class) || type.is(float.class))
        {
            schema.type("number");
            schema.format("float");
            return schema;
        }
        if (type.is(Long.class) || type.is(long.class))
        {
            schema.type("integer");
            schema.format("int64");
            return schema;
        }
        if (type.is(Integer.class) || type.is(int.class))
        {
            schema.type("integer");
            schema.format("int32");
            return schema;
        }
        if (type.is(Short.class) || type.is(short.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Character.class) || type.is(char.class))
        {
            schema.type("integer");
            return schema;
        }
        if (type.is(Byte.class) || type.is(byte.class))
        {
            schema.type("integer");
            return schema;
        }
        return null;
    }
}

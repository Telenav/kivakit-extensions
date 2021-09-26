package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.reflection.Member;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiExcludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMemberFromSuperType;
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
        var schema = readSchema(Type.forClass(MicroservletErrors.class));
        return new Schema<>()
                .name("errors")
                .type("object")
                .description(schema.getDescription())
                .title(schema.getTitle())
                .$ref(new ReferenceResolver().reference(Type.forClass(MicroservletErrors.class)));
    }

    private boolean isArrayType(final Member member)
    {
        return member.type().isDescendantOf(Collection.class) || member.type().isArray();
    }

    /**
     * @param member The member
     * @param typeParameter The name of the member's type parameter (if any)
     * @return An object {@link Schema} if the given type is an array, null otherwise
     */
    private Schema readArray(final Member member, Type<?> typeParameter)
    {
        ensureNotNull(member);

        Schema elementTypeSchema = null;
        final Type<?> type = member.type();
        if (type.is(Array.class))
        {
            elementTypeSchema = readSchema(type.arrayElementType());
        }
        if (type.isDescendantOf(Collection.class))
        {
            final Schema<?> typeParameterSchema = readSchema(typeParameter);
            if (typeParameterSchema != null)
            {
                elementTypeSchema = new SchemaCopier().copy(typeParameterSchema);
            }
            else
            {
                problem("Unable to read schema for: $", typeParameter);
            }
        }

        if (elementTypeSchema != null)
        {
            final var schema = new ArraySchema();
            schema.type("array");
            new AnnotationReader().copyToSchema(member, schema);
            return schema.items(new Schema()
                    .type(elementTypeSchema.getType())
                    .$ref(new ReferenceResolver().reference(typeParameter)));
        }

        return null;
    }

    /**
     * @param type The type to read from
     * @return The object {@link Schema}.
     */
    private Schema<?> readObject(final Type<?> type)
    {
        var primitiveSchema = new PrimitiveReader().readPrimitive(type);
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
            schema.deprecated(annotation.deprecated() ? true : null);
            schema.title(annotation.title());

            // and if the type is an enum,
            if (type.isEnum())
            {
                // add the enum values.
                schema.setEnum(new ArrayList<>(type.enumValues()));
                schema.type("string");
            }

            // For each property
            final var properties = new HashMap<String, Schema>();
            final var required = new ArrayList<String>();
            for (final var property : type.properties(new OpenApiPropertyFilter(type)))
            {
                // get the member,
                var member = property.member();
                final var includeAnnotation = member.annotation(OpenApiIncludeMember.class);

                // and if it's a collection or array,
                if (isArrayType(member))
                {
                    final var superTypeInclude = type.annotation(OpenApiIncludeMemberFromSuperType.class);
                    if (superTypeInclude != null && superTypeInclude.genericType() != null)
                    {
                        properties.put(property.name(), readArray(member, Type.forClass(superTypeInclude.genericType())));
                    }
                    else
                    {
                        // get the generic type parameter,
                        final var parameters = member.genericTypeParameters();
                        if (parameters.size() == 1)
                        {
                            // and add the member as an array of the type parameter type.
                            properties.put(property.name(), readArray(member, parameters.get(0)));
                        }
                        else
                        {
                            var genericType = includeAnnotation.genericType();
                            if (genericType != Void.class)
                            {
                                properties.put(property.name(), readArray(member, Type.forClass(genericType)));
                            }
                            else
                            {
                                problem("Could not determine generic type parameter for: $.$", type.simpleName(), member.name());
                            }
                        }
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
                        if (propertyTypeSchema.getType().equals("object")
                                || (propertyTypeSchema.getEnum() != null && !propertyTypeSchema.getEnum().isEmpty()))
                        {
                            propertySchema.$ref(new ReferenceResolver().reference(property.type()));
                            new SchemaCopier().copy(propertyTypeSchema, propertySchema);
                        }
                        propertySchema.format(propertyTypeSchema.getFormat());
                    }

                    if (includeAnnotation != null)
                    {
                        propertySchema.name(property.name());
                        new AnnotationReader().copyToSchema(member, propertySchema);
                        if (includeAnnotation.required())
                        {
                            required.add(property.name());
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
}

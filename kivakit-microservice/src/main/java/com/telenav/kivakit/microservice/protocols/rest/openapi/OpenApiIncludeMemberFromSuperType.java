package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.internal.protocols.rest.openapi.OpenApiIncludeMemberFromSuperTypeRepeater;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a supertype (class or interface) member (method or field) should be included in the OpenAPI
 * specification for a {@link MicroserviceRestService}
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(OpenApiIncludeMemberFromSuperTypeRepeater.class)
public @interface OpenApiIncludeMemberFromSuperType
{
    /**
     * @return The allowable values for the annotated member
     */
    String[] allowableValues() default {};

    /**
     * @return The default value for the annotated member
     */
    String defaultValue() default "";

    /**
     * @return Description of the annotated member
     */
    String description();

    /**
     * @return An example of the annotated member
     */
    String example() default "";

    /**
     * @return The type of the member if it is an array
     */
    Class<?> genericType() default Void.class;

    /**
     * @return The name of a superclass member to annotate (omitted when annotating fields and methods)
     */
    String member();

    /**
     * @return Reference to a schema for this member
     */
    String reference() default "";

    /**
     * @return True if the annotated member is required
     */
    boolean required() default true;
}

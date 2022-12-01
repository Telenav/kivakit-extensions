package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a supertype (class or interface) member (method or field) should be included in the OpenAPI
 * specification for a {@link RestService}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(OpenApiIncludeMemberFromSuperTypeRepeater.class)
public @interface OpenApiIncludeMemberFromSuperType
{
    /**
     * Returns the allowable values for the annotated member
     */
    String[] allowableValues() default {};

    /**
     * Returns the default value for the annotated member
     */
    String defaultValue() default "";

    /**
     * Returns description of the annotated member
     */
    String description();

    /**
     * Returns an example of the annotated member
     */
    String example() default "";

    /**
     * Returns the type of the member if it is an array
     */
    Class<?> genericType() default Void.class;

    /**
     * Returns the name of a superclass member to annotate (omitted when annotating fields and methods)
     */
    String member();

    /**
     * Returns reference to a schema for this member
     */
    String reference() default "";

    /**
     * Returns true if the annotated member is required
     */
    boolean required() default true;
}

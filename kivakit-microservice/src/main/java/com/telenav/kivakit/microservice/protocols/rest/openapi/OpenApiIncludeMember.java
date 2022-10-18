package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a member (method or field) should be included in the OpenAPI specification produced for a {@link
 * RestService}
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface OpenApiIncludeMember
{
    /**
     * Returns the default value for the annotated member
     */
    String defaultValue() default "";

    /**
     * Returns true if this member is deprecated
     */
    boolean deprecated() default false;

    /**
     * Returns description of the annotated member
     */
    String description();

    /**
     * Returns an example of the annotated member
     */
    String example() default "";

    /**
     * Returns the format of the member
     */
    String format() default "";

    /**
     * Returns the generic type for an array or list member whose generic type cannot be determined
     */
    Class<?> genericType() default Void.class;

    /**
     * Returns true if this members value is nullable
     */
    boolean nullable() default false;

    /**
     * Returns reference to a schema for this member
     */
    String reference() default "";

    /**
     * Returns true if the annotated member is required
     */
    boolean required() default true;

    /**
     * Returns title for this member
     */
    String title() default "";

    /**
     * Returns the type of the member
     */
    String type() default "";
}

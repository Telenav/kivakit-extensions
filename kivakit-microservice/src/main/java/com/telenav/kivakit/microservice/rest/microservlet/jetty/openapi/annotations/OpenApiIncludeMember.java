package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.JettyOpenApiRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to indicate that methods or fields should be included in the OpenAPI specification
 * produced by {@link JettyOpenApiRequest}.
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface OpenApiIncludeMember
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
     * @return True if this member is deprecated
     */
    boolean deprecated() default false;

    /**
     * @return Description of the annotated member
     */
    String description();

    /**
     * @return An example of the annotated member
     */
    String example() default "";

    /**
     * @return The format of the member
     */
    String format() default "";

    /**
     * @return True if this members value is nullable
     */
    boolean nullable() default false;

    /**
     * @return Reference to a schema for this member
     */
    String reference() default "";

    /**
     * @return True if the annotated member is required
     */
    boolean required() default true;

    /**
     * @return The type of the member
     */
    String type() default "";
}

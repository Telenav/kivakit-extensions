package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to indicate members that should be included in the OpenAPI specification for this REST
 * application. When applied to a field or method, no member name is needed. When applied to a class it will include the
 * names of fields and/or methods from the superclass.
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(OpenApiIncludeFromSuperTypeRepeater.class)
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

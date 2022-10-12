package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a type should be included in the OpenAPI specification for a {@link RestService}
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface OpenApiIncludeType
{
    /**
     * Returns true if this schema is deprecated
     */
    boolean deprecated() default false;

    /**
     * Returns description of this schema
     */
    String description();

    /**
     * Returns title of this schema
     */
    String title() default "";
}

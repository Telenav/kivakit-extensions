package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a type should be included in the OpenAPI specification for a {@link RestService}
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OpenApiIncludeType
{
    /**
     * @return True if this schema is deprecated
     */
    boolean deprecated() default false;

    /**
     * @return Description of this schema
     */
    String description();

    /**
     * @return Title of this schema
     */
    String title() default "";
}

package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.JettyOpenApiRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Not public API</b>
 *
 * <p>
 * This annotation can be used to exclude members from a superclass from the OpenAPI specification for a REST
 * application
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OpenApiExclude
{
    /**
     * @return The names of the methods or fields in any supertype that are to be excluded from the OpenAPI
     * specification for a REST application produced by {@link JettyOpenApiRequest}.
     */
    String[] value() default {};
}

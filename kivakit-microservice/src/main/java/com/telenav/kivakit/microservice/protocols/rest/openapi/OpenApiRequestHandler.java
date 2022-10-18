package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Adds metadata to a {@link MicroservletRequest} handler. This metadata is used when producing an OpenAPI specification
 * for a {@link RestService}.
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface OpenApiRequestHandler
{
    /**
     * Returns a full description of this request
     */
    String description() default "";

    /**
     * Returns a short summary of this request
     */
    String summary() default "";

    /**
     * Returns any tags for this request handler
     */
    String[] tags() default {};
}

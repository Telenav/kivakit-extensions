package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds metadata to a {@link MicroservletRequest} handler. This metadata is used when producing an OpenAPI specification
 * for a {@link MicroserviceRestService}.
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenApiRequestHandler
{
    /**
     * @return A full description of this request
     */
    String description() default "";

    /**
     * @return A short summary of this request
     */
    String summary() default "";
}

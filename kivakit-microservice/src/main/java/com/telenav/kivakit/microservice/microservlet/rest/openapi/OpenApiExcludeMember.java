package com.telenav.kivakit.microservice.microservlet.rest.openapi;

import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.openapi.JettyOpenApiRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a supertype (class or interface) member (field or method) should be excluded from the OpenAPI
 * specification for a {@link MicroserviceRestService} even if they were included in the supertype with {@link
 * OpenApiExcludeMember}.
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OpenApiExcludeMember
{
    /**
     * @return The names of the methods or fields in any supertype that are to be excluded from the OpenAPI
     * specification produced by the {@link JettyOpenApiRequest} request handler.
     */
    String[] value() default {};
}

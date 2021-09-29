package com.telenav.kivakit.microservice.rest.microservlet.openapi;

import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds metadata to a microservlet request handler ({@link MicroservletGetRequest#onGet()}, {@link
 * MicroservletPostRequest#onPost()} or {@link MicroservletDeleteRequest#onDelete()}). This metadata is used when
 * producing an OpenAPI specification for a {@link MicroserviceRestApplication}.
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

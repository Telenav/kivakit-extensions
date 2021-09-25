package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to annotate the {@link MicroservletGetRequest#onGet()}, {@link
 * MicroservletPostRequest#onPost()} and {@link MicroservletDeleteRequest#onDelete()} request methods.
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenApiRequest
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

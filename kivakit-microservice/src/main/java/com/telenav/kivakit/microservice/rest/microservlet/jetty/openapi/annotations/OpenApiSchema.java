package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OpenApiSchema
{
    /**
     * @return True if this schema is deprecated
     */
    boolean deprecated() default false;

    /**
     * @return Description of this schema
     */
    String description() default "";

    /**
     * @return Title of this schema
     */
    String title() default "";
}

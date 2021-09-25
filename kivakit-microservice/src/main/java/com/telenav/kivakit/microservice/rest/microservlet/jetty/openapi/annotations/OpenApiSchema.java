package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OpenApiSchema
{
    boolean deprecated() default false;

    String description() default "";

    boolean nullable() default false;

    String title() default "";
}

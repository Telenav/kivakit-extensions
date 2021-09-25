package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Allows the @{@link OpenApiIncludeFromSuperType} annotation to be used multiple times on a type
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OpenApiIncludeFromSuperTypeRepeater
{
    OpenApiIncludeFromSuperType[] value();
}

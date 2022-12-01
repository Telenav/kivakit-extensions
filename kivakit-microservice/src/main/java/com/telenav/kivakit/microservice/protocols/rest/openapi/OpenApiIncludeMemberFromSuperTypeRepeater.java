package com.telenav.kivakit.microservice.protocols.rest.openapi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Allows the @{@link OpenApiIncludeMemberFromSuperType} annotation to be used multiple times on a single type.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface OpenApiIncludeMemberFromSuperTypeRepeater
{
    OpenApiIncludeMemberFromSuperType[] value();
}

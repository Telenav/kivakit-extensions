package com.telenav.kivakit.microservice.protocols.rest.openapi;

import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMemberFromSuperType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Allows the @{@link OpenApiIncludeMemberFromSuperType} annotation to be used multiple times on a single type.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OpenApiIncludeMemberFromSuperTypeRepeater
{
    OpenApiIncludeMemberFromSuperType[] value();
}

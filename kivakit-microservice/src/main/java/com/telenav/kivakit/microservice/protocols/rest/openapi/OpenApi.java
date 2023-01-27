package com.telenav.kivakit.microservice.protocols.rest.openapi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface OpenApi
{
    /**
     * Returns YAML describing the type
     */
    String value();
}

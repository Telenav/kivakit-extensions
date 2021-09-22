package com.telenav.kivakit.microservice.rest.microservlet.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A named measurement of some value")
public class Metric
{
    /** The name of the measurement */
    private final String name;

    /** The measurement value */
    private final Object value;

    public Metric(final String name, final Object value)
    {
        this.name = name;
        this.value = value;
    }
}

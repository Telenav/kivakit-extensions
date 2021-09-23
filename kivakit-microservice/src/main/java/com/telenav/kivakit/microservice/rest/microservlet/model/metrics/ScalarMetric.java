package com.telenav.kivakit.microservice.rest.microservlet.model.metrics;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A named measurement of some value")
public class ScalarMetric<T> extends BaseMetric<T>
{
    /** The name of the measurement */
    private final String name;

    /** The measurement value */
    private final T measurement;

    public ScalarMetric(final String name, final T measurement)
    {
        this.name = name;
        this.measurement = measurement;
    }

    @Override
    public T measurement()
    {
        return measurement;
    }

    @Override
    public String name()
    {
        return name;
    }
}

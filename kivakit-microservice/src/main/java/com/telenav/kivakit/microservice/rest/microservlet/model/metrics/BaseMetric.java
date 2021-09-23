package com.telenav.kivakit.microservice.rest.microservlet.model.metrics;

import com.telenav.kivakit.kernel.language.time.Time;

public abstract class BaseMetric<T> implements Metric<T>
{
    private final Time created = Time.now();

    @Override
    public Time created()
    {
        return created;
    }
}

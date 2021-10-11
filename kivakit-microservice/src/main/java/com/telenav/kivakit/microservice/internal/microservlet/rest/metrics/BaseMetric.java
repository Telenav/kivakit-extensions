package com.telenav.kivakit.microservice.internal.microservlet.rest.metrics;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.microservice.metrics.Metric;

public abstract class BaseMetric<T> implements Metric<T>
{
    private final Time created = Time.now();

    @Override
    public Time created()
    {
        return created;
    }
}

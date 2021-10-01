package com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.QuantumMetric;

public class MaximumMetric<T extends Quantizable> extends QuantumMetric<T>
{
    public MaximumMetric(final MapFactory<Long, T> factory)
    {
        super(factory);
    }

    @Override
    protected long quantumMeasurement()
    {
        return maximum();
    }
}
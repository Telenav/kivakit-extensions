package com.telenav.kivakit.microservice.rest.microservlet.model.metrics.aggregates;

import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.QuantumMetric;

public class MinimumMetric<T extends Quantizable> extends QuantumMetric<T>
{
    public MinimumMetric(final MapFactory<Long, T> factory)
    {
        super(factory);
    }

    @Override
    protected long quantumMeasurement()
    {
        return minimum();
    }
}

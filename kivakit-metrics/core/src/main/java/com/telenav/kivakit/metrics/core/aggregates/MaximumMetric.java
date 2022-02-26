package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.numeric.Quantizable;

public class MaximumMetric<T extends Quantizable> extends AggregateQuantumMetric<T>
{
    public MaximumMetric(MapFactory<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return maximum();
    }
}

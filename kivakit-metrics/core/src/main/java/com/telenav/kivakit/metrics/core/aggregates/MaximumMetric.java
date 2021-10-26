package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;

public class MaximumMetric<T extends Quantizable> extends AggregateQuantumMetric<T>
{
    public MaximumMetric(final MapFactory<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return maximum();
    }
}

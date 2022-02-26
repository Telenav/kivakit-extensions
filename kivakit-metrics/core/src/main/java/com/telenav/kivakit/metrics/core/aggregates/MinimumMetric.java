package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.numeric.Quantizable;

public class MinimumMetric<T extends Quantizable> extends AggregateQuantumMetric<T>
{
    public MinimumMetric(MapFactory<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return minimum();
    }
}

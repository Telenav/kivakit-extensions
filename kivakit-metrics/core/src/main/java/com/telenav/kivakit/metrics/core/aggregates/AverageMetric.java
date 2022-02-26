package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.numeric.Quantizable;

public class AverageMetric<T extends Quantizable> extends AggregateQuantumMetric<T>
{
    public AverageMetric(MapFactory<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return total() / count();
    }
}

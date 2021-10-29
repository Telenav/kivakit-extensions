package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;

public class TotalMetric<T extends Quantizable> extends AggregateQuantumMetric<T>
{
    public TotalMetric(MapFactory<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return total();
    }
}

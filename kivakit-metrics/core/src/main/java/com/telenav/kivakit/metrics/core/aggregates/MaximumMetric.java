package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class MaximumMetric<T extends DoubleValued> extends AggregateQuantumMetric<T>
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

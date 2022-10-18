package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.function.Mapper;
import com.telenav.kivakit.interfaces.value.DoubleValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks a maximum
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MaximumMetric<T extends DoubleValued> extends AggregateMetric<T>
{
    public MaximumMetric(Mapper<Double, T> factory)
    {
        super(factory);
    }

    @Override
    protected double compute()
    {
        return maximumSample();
    }
}

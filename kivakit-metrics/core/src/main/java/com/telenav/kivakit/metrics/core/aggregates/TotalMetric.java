package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.DoubleValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A metric which tracks a total
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class TotalMetric<T extends DoubleValued> extends AggregateMetric<T>
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

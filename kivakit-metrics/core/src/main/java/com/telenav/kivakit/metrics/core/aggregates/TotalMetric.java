package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.DoubleValued;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks a total
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
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

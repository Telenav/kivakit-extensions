package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A metric which tracks a maximum rate
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MaximumRateMetric extends MaximumMetric<Rate>
{
    public MaximumRateMetric()
    {
        super(Rate::perSecond);
    }

    @Override
    public MaximumRateMetric description(String description)
    {
        return (MaximumRateMetric) super.description(description);
    }

    @Override
    public MaximumRateMetric name(String name)
    {
        return (MaximumRateMetric) super.name(name);
    }

    @Override
    public MaximumRateMetric type(MetricType type)
    {
        return (MaximumRateMetric) super.type(type);
    }

    @Override
    public MaximumRateMetric unit(String unit)
    {
        return (MaximumRateMetric) super.unit(unit);
    }
}

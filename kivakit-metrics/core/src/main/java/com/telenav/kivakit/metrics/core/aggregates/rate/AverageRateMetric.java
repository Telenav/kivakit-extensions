package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A metric which tracks an average rate
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class AverageRateMetric extends AverageMetric<Rate>
{
    public AverageRateMetric()
    {
        super(Rate::perSecond);
    }

    @Override
    public AverageRateMetric description(String description)
    {
        return (AverageRateMetric) super.description(description);
    }

    @Override
    public AverageRateMetric name(String name)
    {
        return (AverageRateMetric) super.name(name);
    }

    @Override
    public AverageRateMetric type(MetricType type)
    {
        return (AverageRateMetric) super.type(type);
    }

    @Override
    public AverageRateMetric unit(String unit)
    {
        return (AverageRateMetric) super.unit(unit);
    }
}

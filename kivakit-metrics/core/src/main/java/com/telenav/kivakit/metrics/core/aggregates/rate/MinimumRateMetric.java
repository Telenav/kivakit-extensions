package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks a minimum rate
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MinimumRateMetric extends MinimumMetric<Rate>
{
    public MinimumRateMetric()
    {
        super(Rate::perSecond);
    }

    @Override
    public MinimumRateMetric description(String description)
    {
        return (MinimumRateMetric) super.description(description);
    }

    @Override
    public MinimumRateMetric name(String name)
    {
        return (MinimumRateMetric) super.name(name);
    }

    @Override
    public MinimumRateMetric type(MetricType type)
    {
        return (MinimumRateMetric) super.type(type);
    }

    @Override
    public MinimumRateMetric unit(String unit)
    {
        return (MinimumRateMetric) super.unit(unit);
    }
}

package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

public class MinimumRateMetric extends MinimumMetric<Rate>
{
    public MinimumRateMetric()
    {
        super(Rate::perSecond);
    }

    public MinimumRateMetric description(final String description)
    {
        return (MinimumRateMetric) super.description(description);
    }

    @Override
    public MinimumRateMetric name(final String name)
    {
        return (MinimumRateMetric) super.name(name);
    }

    @Override
    public MinimumRateMetric type(final MetricType type)
    {
        return (MinimumRateMetric) super.type(type);
    }
}

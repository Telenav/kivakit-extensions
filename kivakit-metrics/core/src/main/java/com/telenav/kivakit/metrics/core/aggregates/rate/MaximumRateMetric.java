package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

public class MaximumRateMetric extends MaximumMetric<Rate>
{
    public MaximumRateMetric()
    {
        super(Rate::perSecond);
    }

    @Override
    public MaximumRateMetric description(final String description)
    {
        return (MaximumRateMetric) super.description(description);
    }

    @Override
    public MaximumRateMetric name(final String name)
    {
        return (MaximumRateMetric) super.name(name);
    }

    @Override
    public MaximumRateMetric type(final MetricType type)
    {
        return (MaximumRateMetric) super.type(type);
    }

    @Override
    public MaximumRateMetric unit(final String unit)
    {
        return (MaximumRateMetric) super.unit(unit);
    }
}

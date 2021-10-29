package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

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

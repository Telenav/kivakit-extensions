package com.telenav.kivakit.metrics.core.aggregates.rate;

import com.telenav.kivakit.kernel.language.time.Rate;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageRateMetric extends AverageMetric<Rate>
{
    public AverageRateMetric()
    {
        super(Rate::perSecond);
    }

    @Override
    public AverageRateMetric description(final String description)
    {
        return (AverageRateMetric) super.description(description);
    }

    @Override
    public AverageRateMetric name(final String name)
    {
        return (AverageRateMetric) super.name(name);
    }

    @Override
    public AverageRateMetric type(final MetricType type)
    {
        return (AverageRateMetric) super.type(type);
    }

    @Override
    public AverageRateMetric unit(final String unit)
    {
        return (AverageRateMetric) super.unit(unit);
    }
}

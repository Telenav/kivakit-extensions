package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

public class MinimumDurationMetric extends MinimumMetric<Duration>
{
    public MinimumDurationMetric()
    {
        super(Duration::seconds);
    }

    @Override
    public MinimumDurationMetric description(final String description)
    {
        return (MinimumDurationMetric) super.description(description);
    }

    @Override
    public MinimumDurationMetric name(final String name)
    {
        return (MinimumDurationMetric) super.name(name);
    }

    @Override
    public MinimumDurationMetric type(final MetricType type)
    {
        return (MinimumDurationMetric) super.type(type);
    }

    @Override
    public MinimumDurationMetric unit(final String unit)
    {
        return (MinimumDurationMetric) super.unit(unit);
    }
}
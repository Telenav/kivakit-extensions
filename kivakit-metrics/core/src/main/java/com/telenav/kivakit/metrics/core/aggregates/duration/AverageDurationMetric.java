package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageDurationMetric extends AverageMetric<Duration>
{
    public AverageDurationMetric()
    {
        super(Duration::seconds);
    }

    public AverageDurationMetric description(final String description)
    {
        return (AverageDurationMetric) super.description(description);
    }

    @Override
    public AverageDurationMetric name(final String name)
    {
        return (AverageDurationMetric) super.name(name);
    }

    @Override
    public AverageDurationMetric type(final MetricType type)
    {
        return (AverageDurationMetric) super.type(type);
    }

    @Override
    public AverageDurationMetric unit(final String unit)
    {
        return (AverageDurationMetric) super.unit(unit);
    }
}

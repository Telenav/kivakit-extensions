package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageDurationMetric extends AverageMetric<Duration>
{
    public AverageDurationMetric()
    {
        super(Duration::seconds);
    }

    public AverageDurationMetric description(String description)
    {
        return (AverageDurationMetric) super.description(description);
    }

    @Override
    public AverageDurationMetric name(String name)
    {
        return (AverageDurationMetric) super.name(name);
    }

    @Override
    public AverageDurationMetric type(MetricType type)
    {
        return (AverageDurationMetric) super.type(type);
    }

    @Override
    public AverageDurationMetric unit(String unit)
    {
        return (AverageDurationMetric) super.unit(unit);
    }
}

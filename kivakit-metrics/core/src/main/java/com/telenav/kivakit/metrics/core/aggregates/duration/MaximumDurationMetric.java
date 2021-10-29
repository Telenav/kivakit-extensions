package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

public class MaximumDurationMetric extends MaximumMetric<Duration>
{
    public MaximumDurationMetric()
    {
        super(Duration::seconds);
    }

    @Override
    public MaximumDurationMetric description(String description)
    {
        return (MaximumDurationMetric) super.description(description);
    }

    @Override
    public MaximumDurationMetric name(String name)
    {
        return (MaximumDurationMetric) super.name(name);
    }

    @Override
    public MaximumDurationMetric type(MetricType type)
    {
        return (MaximumDurationMetric) super.type(type);
    }

    @Override
    public MaximumDurationMetric unit(String unit)
    {
        return (MaximumDurationMetric) super.unit(unit);
    }
}

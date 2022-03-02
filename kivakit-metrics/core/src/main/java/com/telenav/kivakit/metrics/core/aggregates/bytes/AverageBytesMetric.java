package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageBytesMetric extends AverageMetric<Bytes>
{
    public AverageBytesMetric()
    {
        super(Bytes::bytes);
    }

    public AverageBytesMetric description(String description)
    {
        return (AverageBytesMetric) super.description(description);
    }

    @Override
    public AverageBytesMetric name(String name)
    {
        return (AverageBytesMetric) super.name(name);
    }

    @Override
    public AverageBytesMetric type(MetricType type)
    {
        return (AverageBytesMetric) super.type(type);
    }

    @Override
    public AverageBytesMetric unit(String unit)
    {
        return (AverageBytesMetric) super.unit(unit);
    }
}

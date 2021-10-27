package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageBytesMetric extends AverageMetric<Bytes>
{
    public AverageBytesMetric()
    {
        super(Bytes::bytes);
    }

    public AverageBytesMetric description(final String description)
    {
        return (AverageBytesMetric) super.description(description);
    }

    @Override
    public AverageBytesMetric name(final String name)
    {
        return (AverageBytesMetric) super.name(name);
    }

    @Override
    public AverageBytesMetric type(final MetricType type)
    {
        return (AverageBytesMetric) super.type(type);
    }

    @Override
    public AverageBytesMetric unit(final String unit)
    {
        return (AverageBytesMetric) super.unit(unit);
    }
}

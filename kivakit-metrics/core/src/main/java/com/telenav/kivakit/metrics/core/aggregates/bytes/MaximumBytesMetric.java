package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

public class MaximumBytesMetric extends MaximumMetric<Bytes>
{
    public MaximumBytesMetric()
    {
        super(Bytes::bytes);
    }

    public MaximumBytesMetric description(String description)
    {
        return (MaximumBytesMetric) super.description(description);
    }

    @Override
    public MaximumBytesMetric name(String name)
    {
        return (MaximumBytesMetric) super.name(name);
    }

    @Override
    public MaximumBytesMetric type(MetricType type)
    {
        return (MaximumBytesMetric) super.type(type);
    }

    @Override
    public MaximumBytesMetric unit(String unit)
    {
        return (MaximumBytesMetric) super.unit(unit);
    }
}

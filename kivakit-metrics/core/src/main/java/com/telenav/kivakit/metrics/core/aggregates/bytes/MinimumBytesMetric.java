package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

public class MinimumBytesMetric extends MinimumMetric<Bytes>
{
    public MinimumBytesMetric()
    {
        super(Bytes::bytes);
    }

    @Override
    public MinimumBytesMetric description(String description)
    {
        return (MinimumBytesMetric) super.description(description);
    }

    @Override
    public MinimumBytesMetric name(String name)
    {
        return (MinimumBytesMetric) super.name(name);
    }

    @Override
    public MinimumBytesMetric type(MetricType type)
    {
        return (MinimumBytesMetric) super.type(type);
    }

    @Override
    public MinimumBytesMetric unit(String unit)
    {
        return (MinimumBytesMetric) super.unit(unit);
    }
}

package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

public class TotalBytesMetric extends TotalMetric<Bytes>
{
    public TotalBytesMetric()
    {
        super(Bytes::bytes);
    }

    @Override
    public TotalBytesMetric description(String description)
    {
        return (TotalBytesMetric) super.description(description);
    }

    @Override
    public TotalBytesMetric name(String name)
    {
        return (TotalBytesMetric) super.name(name);
    }

    @Override
    public TotalBytesMetric type(MetricType type)
    {
        return (TotalBytesMetric) super.type(type);
    }

    @Override
    public TotalBytesMetric unit(String unit)
    {
        return (TotalBytesMetric) super.unit(unit);
    }
}

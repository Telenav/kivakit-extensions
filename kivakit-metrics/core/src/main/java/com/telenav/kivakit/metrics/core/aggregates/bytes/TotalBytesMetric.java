package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

public class TotalBytesMetric extends TotalMetric<Bytes>
{
    public TotalBytesMetric()
    {
        super(Bytes::bytes);
    }

    public TotalBytesMetric description(final String description)
    {
        return (TotalBytesMetric) super.description(description);
    }

    @Override
    public TotalBytesMetric name(final String name)
    {
        return (TotalBytesMetric) super.name(name);
    }

    @Override
    public TotalBytesMetric type(final MetricType type)
    {
        return (TotalBytesMetric) super.type(type);
    }
}

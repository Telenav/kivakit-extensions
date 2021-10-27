package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

public class MinimumBytesMetric extends MinimumMetric<Bytes>
{
    public MinimumBytesMetric()
    {
        super(Bytes::bytes);
    }

    @Override
    public MinimumBytesMetric description(final String description)
    {
        return (MinimumBytesMetric) super.description(description);
    }

    @Override
    public MinimumBytesMetric name(final String name)
    {
        return (MinimumBytesMetric) super.name(name);
    }

    @Override
    public MinimumBytesMetric type(final MetricType type)
    {
        return (MinimumBytesMetric) super.type(type);
    }

    @Override
    public MinimumBytesMetric unit(final String unit)
    {
        return (MinimumBytesMetric) super.unit(unit);
    }
}

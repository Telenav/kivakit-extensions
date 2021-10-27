package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

public class MaximumBytesMetric extends MaximumMetric<Bytes>
{
    public MaximumBytesMetric()
    {
        super(Bytes::bytes);
    }

    public MaximumBytesMetric description(final String description)
    {
        return (MaximumBytesMetric) super.description(description);
    }

    @Override
    public MaximumBytesMetric name(final String name)
    {
        return (MaximumBytesMetric) super.name(name);
    }

    @Override
    public MaximumBytesMetric type(final MetricType type)
    {
        return (MaximumBytesMetric) super.type(type);
    }

    @Override
    public MaximumBytesMetric unit(final String unit)
    {
        return (MaximumBytesMetric) super.unit(unit);
    }
}

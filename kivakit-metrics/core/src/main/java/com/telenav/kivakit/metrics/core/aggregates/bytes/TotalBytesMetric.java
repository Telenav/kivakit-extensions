package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks the total number of bytes
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
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

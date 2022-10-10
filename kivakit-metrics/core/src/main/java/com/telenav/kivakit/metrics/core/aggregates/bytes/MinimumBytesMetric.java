package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks the minimum number of bytes
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
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

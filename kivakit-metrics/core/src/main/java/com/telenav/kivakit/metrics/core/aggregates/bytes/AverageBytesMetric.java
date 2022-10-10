package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks the average number of bytes
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class AverageBytesMetric extends AverageMetric<Bytes>
{
    public AverageBytesMetric()
    {
        super(Bytes::bytes);
    }

    @Override
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

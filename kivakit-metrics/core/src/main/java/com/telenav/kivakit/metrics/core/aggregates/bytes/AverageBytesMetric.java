package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks the average number of bytes
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
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

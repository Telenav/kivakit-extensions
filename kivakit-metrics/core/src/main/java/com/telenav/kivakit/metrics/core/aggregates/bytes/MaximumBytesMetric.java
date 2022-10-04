package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks the maximum number of bytes
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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

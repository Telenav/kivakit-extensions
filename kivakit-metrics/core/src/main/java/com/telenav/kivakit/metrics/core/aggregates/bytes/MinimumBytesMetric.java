package com.telenav.kivakit.metrics.core.aggregates.bytes;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks the minimum number of bytes
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
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

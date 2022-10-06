package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks a maximum duration
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MaximumDurationMetric extends MaximumMetric<Duration>
{
    public MaximumDurationMetric()
    {
        super(Duration::seconds);
    }

    @Override
    public MaximumDurationMetric description(String description)
    {
        return (MaximumDurationMetric) super.description(description);
    }

    @Override
    public MaximumDurationMetric name(String name)
    {
        return (MaximumDurationMetric) super.name(name);
    }

    @Override
    public MaximumDurationMetric type(MetricType type)
    {
        return (MaximumDurationMetric) super.type(type);
    }

    @Override
    public MaximumDurationMetric unit(String unit)
    {
        return (MaximumDurationMetric) super.unit(unit);
    }
}

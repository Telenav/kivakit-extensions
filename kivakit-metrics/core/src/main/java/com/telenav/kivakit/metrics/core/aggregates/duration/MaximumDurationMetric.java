package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks a maximum duration
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
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

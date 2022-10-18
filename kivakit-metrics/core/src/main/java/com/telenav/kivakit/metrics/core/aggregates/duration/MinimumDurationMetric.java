package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks a minimum duration
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MinimumDurationMetric extends MinimumMetric<Duration>
{
    public MinimumDurationMetric()
    {
        super(Duration::seconds);
    }

    @Override
    public MinimumDurationMetric description(String description)
    {
        return (MinimumDurationMetric) super.description(description);
    }

    @Override
    public MinimumDurationMetric name(String name)
    {
        return (MinimumDurationMetric) super.name(name);
    }

    @Override
    public MinimumDurationMetric type(MetricType type)
    {
        return (MinimumDurationMetric) super.type(type);
    }

    @Override
    public MinimumDurationMetric unit(String unit)
    {
        return (MinimumDurationMetric) super.unit(unit);
    }
}

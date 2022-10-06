package com.telenav.kivakit.metrics.core.aggregates.duration;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks an average duration
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class AverageDurationMetric extends AverageMetric<Duration>
{
    public AverageDurationMetric()
    {
        super(Duration::seconds);
    }

    public AverageDurationMetric description(String description)
    {
        return (AverageDurationMetric) super.description(description);
    }

    @Override
    public AverageDurationMetric name(String name)
    {
        return (AverageDurationMetric) super.name(name);
    }

    @Override
    public AverageDurationMetric type(MetricType type)
    {
        return (AverageDurationMetric) super.type(type);
    }

    @Override
    public AverageDurationMetric unit(String unit)
    {
        return (AverageDurationMetric) super.unit(unit);
    }
}

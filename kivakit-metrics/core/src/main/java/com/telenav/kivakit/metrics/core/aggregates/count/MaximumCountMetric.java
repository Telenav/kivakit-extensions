package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A metric which tracks a maximum count
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MaximumCountMetric extends MaximumMetric<Count>
{
    public MaximumCountMetric()
    {
        super(Count::count);
    }

    public MaximumCountMetric description(String description)
    {
        return (MaximumCountMetric) super.description(description);
    }

    @Override
    public MaximumCountMetric name(String name)
    {
        return (MaximumCountMetric) super.name(name);
    }

    @Override
    public MaximumCountMetric type(MetricType type)
    {
        return (MaximumCountMetric) super.type(type);
    }

    @Override
    public MaximumCountMetric unit(String unit)
    {
        return (MaximumCountMetric) super.unit(unit);
    }
}

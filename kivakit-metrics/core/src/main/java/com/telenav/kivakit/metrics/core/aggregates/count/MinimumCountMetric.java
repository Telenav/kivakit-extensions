package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks a minimum count
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MinimumCountMetric extends MinimumMetric<Count>
{
    public MinimumCountMetric()
    {
        super(Count::count);
    }

    @Override
    public MinimumCountMetric description(String description)
    {
        return (MinimumCountMetric) super.description(description);
    }

    @Override
    public MinimumCountMetric name(String name)
    {
        return (MinimumCountMetric) super.name(name);
    }

    @Override
    public MinimumCountMetric type(MetricType type)
    {
        return (MinimumCountMetric) super.type(type);
    }

    @Override
    public MinimumCountMetric unit(String unit)
    {
        return (MinimumCountMetric) super.unit(unit);
    }
}

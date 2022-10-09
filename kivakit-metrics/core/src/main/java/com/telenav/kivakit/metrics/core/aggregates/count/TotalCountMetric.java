package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * A metric which tracks a total count
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class TotalCountMetric extends TotalMetric<Count>
{
    public TotalCountMetric()
    {
        super(Count::count);
    }

    @Override
    public TotalCountMetric description(String description)
    {
        return (TotalCountMetric) super.description(description);
    }

    @Override
    public TotalCountMetric name(String name)
    {
        return (TotalCountMetric) super.name(name);
    }

    @Override
    public TotalCountMetric type(MetricType type)
    {
        return (TotalCountMetric) super.type(type);
    }

    @Override
    public TotalCountMetric unit(String unit)
    {
        return (TotalCountMetric) super.unit(unit);
    }
}

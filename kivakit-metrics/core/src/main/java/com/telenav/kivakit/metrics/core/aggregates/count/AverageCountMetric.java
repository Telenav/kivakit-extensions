package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A metric which tracks an average count
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class AverageCountMetric extends AverageMetric<Count>
{
    public AverageCountMetric()
    {
        super(Count::count);
    }

    public AverageCountMetric description(String description)
    {
        return (AverageCountMetric) super.description(description);
    }

    @Override
    public AverageCountMetric name(String name)
    {
        return (AverageCountMetric) super.name(name);
    }

    @Override
    public AverageCountMetric type(MetricType type)
    {
        return (AverageCountMetric) super.type(type);
    }

    @Override
    public AverageCountMetric unit(String unit)
    {
        return (AverageCountMetric) super.unit(unit);
    }
}

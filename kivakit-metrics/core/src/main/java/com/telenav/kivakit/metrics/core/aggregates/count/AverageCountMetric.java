package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks an average count
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
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

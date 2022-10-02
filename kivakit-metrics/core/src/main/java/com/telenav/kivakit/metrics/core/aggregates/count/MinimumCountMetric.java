package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A metric which tracks a minimum count
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
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

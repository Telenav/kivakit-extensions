package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

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

package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.language.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

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

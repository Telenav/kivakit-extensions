package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.language.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

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

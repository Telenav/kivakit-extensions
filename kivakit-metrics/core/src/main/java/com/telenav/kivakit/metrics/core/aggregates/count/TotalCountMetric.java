package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

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

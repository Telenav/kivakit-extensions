package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.TotalMetric;

public class TotalCountMetric extends TotalMetric<Count>
{
    public TotalCountMetric()
    {
        super(Count::count);
    }

    @Override
    public TotalCountMetric description(final String description)
    {
        return (TotalCountMetric) super.description(description);
    }

    @Override
    public TotalCountMetric name(final String name)
    {
        return (TotalCountMetric) super.name(name);
    }

    @Override
    public TotalCountMetric type(final MetricType type)
    {
        return (TotalCountMetric) super.type(type);
    }

    @Override
    public TotalCountMetric unit(final String unit)
    {
        return (TotalCountMetric) super.unit(unit);
    }
}

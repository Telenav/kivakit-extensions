package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MinimumMetric;

public class MinimumCountMetric extends MinimumMetric<Count>
{
    public MinimumCountMetric()
    {
        super(Count::count);
    }

    @Override
    public MinimumCountMetric description(final String description)
    {
        return (MinimumCountMetric) super.description(description);
    }

    @Override
    public MinimumCountMetric name(final String name)
    {
        return (MinimumCountMetric) super.name(name);
    }

    @Override
    public MinimumCountMetric type(final MetricType type)
    {
        return (MinimumCountMetric) super.type(type);
    }

    @Override
    public MinimumCountMetric unit(final String unit)
    {
        return (MinimumCountMetric) super.unit(unit);
    }
}

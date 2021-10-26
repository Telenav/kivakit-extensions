package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.AverageMetric;

public class AverageCountMetric extends AverageMetric<Count>
{
    public AverageCountMetric()
    {
        super(Count::count);
    }

    public AverageCountMetric description(final String description)
    {
        return (AverageCountMetric) super.description(description);
    }

    @Override
    public AverageCountMetric name(final String name)
    {
        return (AverageCountMetric) super.name(name);
    }

    @Override
    public AverageCountMetric type(final MetricType type)
    {
        return (AverageCountMetric) super.type(type);
    }
}

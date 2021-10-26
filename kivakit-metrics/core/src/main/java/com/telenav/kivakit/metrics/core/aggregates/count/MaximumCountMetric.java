package com.telenav.kivakit.metrics.core.aggregates.count;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.metrics.core.aggregates.MaximumMetric;

public class MaximumCountMetric extends MaximumMetric<Count>
{
    public MaximumCountMetric()
    {
        super(Count::count);
    }

    public MaximumCountMetric description(final String description)
    {
        return (MaximumCountMetric) super.description(description);
    }

    @Override
    public MaximumCountMetric name(final String name)
    {
        return (MaximumCountMetric) super.name(name);
    }

    @Override
    public MaximumCountMetric type(final MetricType type)
    {
        return (MaximumCountMetric) super.type(type);
    }
}

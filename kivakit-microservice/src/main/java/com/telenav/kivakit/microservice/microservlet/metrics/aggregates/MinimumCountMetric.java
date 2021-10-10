package com.telenav.kivakit.microservice.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Count;

public class MinimumCountMetric extends MinimumMetric<Count>
{
    public MinimumCountMetric()
    {
        super(Count::count);
    }
}

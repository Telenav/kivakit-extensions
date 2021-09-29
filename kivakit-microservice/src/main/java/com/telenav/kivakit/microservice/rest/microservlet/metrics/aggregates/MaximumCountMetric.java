package com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Count;

public class MaximumCountMetric extends MaximumMetric<Count>
{
    public MaximumCountMetric()
    {
        super(Count::count);
    }
}

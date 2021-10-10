package com.telenav.kivakit.microservice.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Count;

public class TotalCountMetric extends TotalMetric<Count>
{
    public TotalCountMetric()
    {
        super(Count::count);
    }
}

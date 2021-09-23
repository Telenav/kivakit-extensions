package com.telenav.kivakit.microservice.rest.microservlet.model.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Count;

public class AverageCountMetric extends AverageMetric<Count>
{
    public AverageCountMetric()
    {
        super(Count::count);
    }
}

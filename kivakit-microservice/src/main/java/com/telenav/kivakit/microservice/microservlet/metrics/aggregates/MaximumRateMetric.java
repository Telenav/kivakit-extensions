package com.telenav.kivakit.microservice.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.time.Rate;

public class MaximumRateMetric extends MaximumMetric<Rate>
{
    public MaximumRateMetric()
    {
        super(Rate::perYear);
    }
}

package com.telenav.kivakit.microservice.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.time.Rate;

public class MinimumRateMetric extends MinimumMetric<Rate>
{
    public MinimumRateMetric()
    {
        super(Rate::perYear);
    }
}

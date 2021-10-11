package com.telenav.kivakit.microservice.metrics.aggregates;

import com.telenav.kivakit.kernel.language.time.Rate;

public class AverageRateMetric extends AverageMetric<Rate>
{
    public AverageRateMetric()
    {
        super(Rate::perYear);
    }
}

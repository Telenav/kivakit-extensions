package com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Bytes;

public class AverageBytesMetric extends AverageMetric<Bytes>
{
    public AverageBytesMetric()
    {
        super(Bytes::bytes);
    }
}
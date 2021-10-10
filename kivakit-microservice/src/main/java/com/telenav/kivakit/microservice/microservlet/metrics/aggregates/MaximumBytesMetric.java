package com.telenav.kivakit.microservice.microservlet.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Bytes;

public class MaximumBytesMetric extends MaximumMetric<Bytes>
{
    public MaximumBytesMetric()
    {
        super(Bytes::bytes);
    }
}

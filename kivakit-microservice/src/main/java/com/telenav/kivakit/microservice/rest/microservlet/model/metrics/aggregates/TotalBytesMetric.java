package com.telenav.kivakit.microservice.rest.microservlet.model.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Bytes;

public class TotalBytesMetric extends TotalMetric<Bytes>
{
    public TotalBytesMetric()
    {
        super(Bytes::bytes);
    }
}

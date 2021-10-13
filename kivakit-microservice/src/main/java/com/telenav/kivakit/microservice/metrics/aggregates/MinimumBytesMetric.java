package com.telenav.kivakit.microservice.metrics.aggregates;

import com.telenav.kivakit.kernel.language.values.count.Bytes;

public class MinimumBytesMetric extends MinimumMetric<Bytes>
{
    public MinimumBytesMetric()
    {
        super(Bytes::bytes);
    }
}
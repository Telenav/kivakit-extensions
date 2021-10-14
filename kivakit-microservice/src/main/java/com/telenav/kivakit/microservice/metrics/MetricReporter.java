package com.telenav.kivakit.microservice.metrics;

import java.util.List;

public interface MetricReporter
{
    /**
     * @param metrics The list of metrics to report
     */
    void report(List<Metric<?>> metrics);
}

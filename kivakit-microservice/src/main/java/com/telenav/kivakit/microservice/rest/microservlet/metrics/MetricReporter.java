package com.telenav.kivakit.microservice.rest.microservlet.metrics;

import java.util.List;

public interface MetricReporter
{
    /**
     * @param metrics The list of metrics to report
     */
    void report(List<Metric<?>> metrics);
}

package com.telenav.kivakit.microservice.internal.microservlet.rest.metrics;

import com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.metrics.Metric;
import com.telenav.kivakit.microservice.metrics.ScalarMetric;

public interface MetricReportingTrait
{
    /**
     * Reports the given name, value pair as a {@link ScalarMetric} to the request cycle.
     */
    default <T> void metric(String name, T value)
    {
        metric(new ScalarMetric<>(name, value));
    }

    /**
     * Reports the given metric to the request cycle.
     */
    default <T> void metric(Metric<T> metric)
    {
        JettyMicroservletRequestCycle.cycle().add(metric);
    }
}

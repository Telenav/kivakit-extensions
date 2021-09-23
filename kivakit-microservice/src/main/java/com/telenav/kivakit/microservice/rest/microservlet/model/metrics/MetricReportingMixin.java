package com.telenav.kivakit.microservice.rest.microservlet.model.metrics;

import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;

public interface MetricReportingMixin
{
    default void metric(String name, Object value)
    {
        var metric = new ScalarMetric(name, value);
        final var cycle = JettyMicroservletRequestCycle.cycle();
        var json = cycle.gson().toJson(metric);
        cycle.add(metric);
    }
}

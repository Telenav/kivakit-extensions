package com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.none;

import com.telenav.kivakit.microservice.rest.microservlet.metrics.Metric;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.MetricReporter;

import java.util.List;

/**
 * A {@link MetricReporter} that does nothing
 *
 * @author jonathanl (shibo)
 */
public class NullMetricReporter implements MetricReporter
{
    @Override
    public void report(final List<Metric<?>> metrics)
    {
    }
}

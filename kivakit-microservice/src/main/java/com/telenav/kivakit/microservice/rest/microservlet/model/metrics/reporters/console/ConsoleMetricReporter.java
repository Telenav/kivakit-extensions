package com.telenav.kivakit.microservice.rest.microservlet.model.metrics.reporters.console;

import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.MetricReporter;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.ScalarMetric;

import java.util.List;

public class ConsoleMetricReporter implements MetricReporter
{
    @Override
    public void report(final List<ScalarMetric<?>> metrics)
    {
        for (var metric : metrics)
        {
            Message.println(metric.toString());
        }
    }
}

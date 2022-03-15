package com.telenav.kivakit.metrics.core.reporters;

import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;

public class ConsoleMetricsReporter implements MetricsReporter
{
    @Override
    public void report(Metric<?> metric)
    {
        Console.println(metric.toString());
    }
}

package com.telenav.kivakit.metrics.core.reporters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.os.Console.console;

/**
 * Reports metrics to the console
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ConsoleMetricsReporter implements MetricsReporter
{
    @Override
    public void report(Metric<?> metric)
    {
        console().println(metric.toString());
    }
}

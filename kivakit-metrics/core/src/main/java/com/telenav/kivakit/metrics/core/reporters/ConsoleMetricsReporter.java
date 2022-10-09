package com.telenav.kivakit.metrics.core.reporters;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Reports metrics to the console
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ConsoleMetricsReporter implements MetricsReporter
{
    @Override
    public void report(Metric<?> metric)
    {
        Console.println(metric.toString());
    }
}

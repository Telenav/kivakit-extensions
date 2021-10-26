package com.telenav.kivakit.metrics.core.reporters;

import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;

/**
 * A {@link MetricsReporter} that does nothing
 *
 * @author jonathanl (shibo)
 */
public class NullMetricsReporter implements MetricsReporter
{
    @Override
    public void report(Metric<?> metric)
    {
    }
}

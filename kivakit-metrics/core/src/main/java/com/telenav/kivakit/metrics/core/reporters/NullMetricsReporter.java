package com.telenav.kivakit.metrics.core.reporters;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A {@link MetricsReporter} that does nothing
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class NullMetricsReporter implements MetricsReporter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void report(Metric<?> metric)
    {
    }
}

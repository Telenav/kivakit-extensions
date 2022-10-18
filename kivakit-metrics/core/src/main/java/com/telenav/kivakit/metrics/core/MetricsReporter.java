package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Reports a series of measurements via {@link #report(Metric)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface MetricsReporter
{
    /**
     * Reports the given metric
     *
     * @param metric The measurement to report
     */
    void report(Metric<?> metric);
}

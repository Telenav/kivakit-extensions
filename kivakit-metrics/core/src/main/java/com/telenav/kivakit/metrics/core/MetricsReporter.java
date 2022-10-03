package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Reports a series of measurements via {@link #report(Metric)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = API_STABLE,
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

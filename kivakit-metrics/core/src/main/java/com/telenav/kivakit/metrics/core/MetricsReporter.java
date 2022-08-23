package com.telenav.kivakit.metrics.core;

/**
 * Reports a series of measurements via {@link #report(Metric)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface MetricsReporter
{
    /**
     * Reports the given metric
     *
     * @param metric The measurement to report
     */
    void report(Metric<?> metric);
}

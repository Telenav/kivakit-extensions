package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.interfaces.time.CreatedAt;

/**
 * A named measurement {@link #created()} at some point in time
 *
 * @author jonathanl (shibo)
 */
public interface Metric<T> extends Named, CreatedAt, Quantizable
{
    enum MetricType
    {
        /** The metric represents a count of something, like requests */
        COUNT,

        /** The metric represents a level, such as CPU or memory use */
        LEVEL,

        /** The metric is an observation in a histogram, such as the duration of a request */
        OBSERVATION
    }

    /**
     * Human-readable description of this metric
     */
    String description();

    /**
     * @return The measurement
     */
    T measurement();

    /**
     * @return The type of measurement
     */
    MetricType type();

    /**
     * @return The metric unit
     */
    String unit();
}

package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.core.time.CreatedAt;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.numeric.Quantizable;

/**
 * A named measurement {@link #createdAt()} at some point in time
 *
 * @author jonathanl (shibo)
 */
public interface Metric<T> extends
        Named,
        CreatedAt,
        Quantizable
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

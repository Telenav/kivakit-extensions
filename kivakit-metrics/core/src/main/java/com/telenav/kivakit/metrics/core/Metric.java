package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.time.CreatedAt;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.value.DoubleValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A named measurement {@link #createdAt()} at some point in time
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface Metric<T> extends
        Named,
        CreatedAt,
        DoubleValued
{
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
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
     * Returns the measurement
     */
    T measurement();

    /**
     * Returns the type of measurement
     */
    MetricType type();

    /**
     * Returns the metric unit
     */
    String unit();
}

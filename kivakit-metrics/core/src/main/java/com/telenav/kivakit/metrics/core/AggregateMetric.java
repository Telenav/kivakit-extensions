package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.interfaces.collection.Addable;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * An aggregate metric is a metric for a set of measurements, added with {@link Addable#add(Object)}
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface AggregateMetric<T> extends
        Addable<T>,
        Metric<T>
{
}

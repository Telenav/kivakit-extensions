package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.interfaces.collection.Addable;

/**
 * An aggregate metric is a metric for a set of measurements, added with {@link Addable#add(Object)}
 *
 * @author jonathanl (shibo)
 */
public interface AggregateMetric<T> extends
        Addable<T>,
        Metric<T>
{
}

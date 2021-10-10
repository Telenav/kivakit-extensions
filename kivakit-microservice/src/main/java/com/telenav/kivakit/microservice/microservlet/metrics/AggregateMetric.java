package com.telenav.kivakit.microservice.microservlet.metrics;

import com.telenav.kivakit.kernel.interfaces.collection.Addable;

public interface AggregateMetric<T> extends Metric<T>, Addable<T>
{
}
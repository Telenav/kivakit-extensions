package com.telenav.kivakit.microservice.rest.microservlet.metrics;

import com.telenav.kivakit.kernel.interfaces.collection.Addable;

public interface AggregateMetric<T> extends Metric<T>, Addable<T>
{
}
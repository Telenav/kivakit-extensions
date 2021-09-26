package com.telenav.kivakit.microservice.rest.microservlet.model.metrics;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.interfaces.time.CreatedAt;

/**
 * A named measurement
 */
public interface Metric<T> extends Named, CreatedAt
{
    T measurement();
}

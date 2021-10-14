package com.telenav.kivakit.microservice.metrics;

import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.microservice.internal.protocols.rest.metrics.BaseMetric;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;

@OpenApiIncludeType(description = "A named measurement of some value")
public class ScalarMetric<T> extends BaseMetric<T>
{
    /** The name of the measurement */
    private final String name;

    /** The measurement value */
    private final T measurement;

    public ScalarMetric(final String name, final T measurement)
    {
        this.name = name;
        this.measurement = measurement;
    }

    @Override
    @KivaKitIncludeProperty
    public T measurement()
    {
        return measurement;
    }

    @Override
    @KivaKitIncludeProperty
    public String name()
    {
        return name;
    }

    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}

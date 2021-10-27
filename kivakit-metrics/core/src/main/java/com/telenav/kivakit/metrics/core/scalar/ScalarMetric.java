package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.metrics.core.AggregateMetric;
import com.telenav.kivakit.metrics.core.BaseMetric;

/**
 * A scalar metric is a single measurement (as opposed to an {@link AggregateMetric}) with a name and a value.
 *
 * @author jonathanl (shibo)
 */
public class ScalarMetric<T extends Quantizable> extends BaseMetric<T>
{
    /** The measurement value */
    private T measurement;

    public ScalarMetric(ScalarMetric<T> that)
    {
        super(that);
        this.measurement = that.measurement;
    }

    public ScalarMetric()
    {
    }

    @Override
    public ScalarMetric<T> description(final String description)
    {
        return (ScalarMetric<T>) super.description(description);
    }

    @Override
    public double doubleQuantum()
    {
        return measurement.quantum();
    }

    @Override
    @KivaKitIncludeProperty
    public T measurement()
    {
        return measurement;
    }

    @Override
    public ScalarMetric<T> name(final String name)
    {
        return (ScalarMetric<T>) super.name(name);
    }

    @Override
    public long quantum()
    {
        return measurement.quantum();
    }

    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @Override
    public ScalarMetric<T> type(final MetricType type)
    {
        return (ScalarMetric<T>) super.type(type);
    }

    @Override
    public ScalarMetric<T> unit(final String unit)
    {
        return (ScalarMetric<T>) super.unit(unit);
    }

    public ScalarMetric<T> withMeasurement(T measurement)
    {
        var copy = new ScalarMetric<>(this);
        copy.measurement = measurement;
        return copy;
    }
}

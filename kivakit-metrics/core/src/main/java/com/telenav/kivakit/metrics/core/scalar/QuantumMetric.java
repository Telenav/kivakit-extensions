package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.metrics.core.AggregateMetric;
import com.telenav.kivakit.metrics.core.BaseMetric;

/**
 * A quantum metric is a single measurement (as opposed to an {@link AggregateMetric}) with a {@link #name()}, {@link
 * #type()}, {@link #unit()}, {@link #description()} and {@link #measurement()}.
 *
 * @author jonathanl (shibo)
 */
public class QuantumMetric<T extends Quantizable> extends BaseMetric<T>
{
    /** The measurement value */
    private T measurement;

    public QuantumMetric(QuantumMetric<T> that)
    {
        super(that);
        this.measurement = that.measurement;
    }

    public QuantumMetric()
    {
    }

    @Override
    public QuantumMetric<T> description(final String description)
    {
        return (QuantumMetric<T>) super.description(description);
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
    public QuantumMetric<T> name(final String name)
    {
        return (QuantumMetric<T>) super.name(name);
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
    public QuantumMetric<T> type(final MetricType type)
    {
        return (QuantumMetric<T>) super.type(type);
    }

    @Override
    public QuantumMetric<T> unit(final String unit)
    {
        return (QuantumMetric<T>) super.unit(unit);
    }

    public QuantumMetric<T> withMeasurement(T measurement)
    {
        var copy = new QuantumMetric<T>(this);
        copy.measurement = measurement;
        return copy;
    }
}

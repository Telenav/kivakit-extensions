package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.kivakit.metrics.core.AggregateMetric;
import com.telenav.kivakit.metrics.core.BaseMetric;

/**
 * A quantum metric is a single measurement (as opposed to an {@link AggregateMetric}) with a {@link #name()},
 * {@link #type()}, {@link #unit()}, {@link #description()} and {@link #measurement()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class DoubleValuedMetric<T extends DoubleValued> extends BaseMetric<T>
{
    /** The measurement value */
    private T measurement;

    public DoubleValuedMetric(DoubleValuedMetric<T> that)
    {
        super(that);
        measurement = that.measurement;
    }

    public DoubleValuedMetric()
    {
    }

    @Override
    public DoubleValuedMetric<T> description(String description)
    {
        return (DoubleValuedMetric<T>) super.description(description);
    }

    @Override
    public double doubleValue()
    {
        return measurement.doubleValue();
    }

    @Override
    @KivaKitIncludeProperty
    public T measurement()
    {
        return measurement;
    }

    @Override
    public DoubleValuedMetric<T> name(String name)
    {
        return (DoubleValuedMetric<T>) super.name(name);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @Override
    public DoubleValuedMetric<T> type(MetricType type)
    {
        return (DoubleValuedMetric<T>) super.type(type);
    }

    @Override
    public DoubleValuedMetric<T> unit(String unit)
    {
        return (DoubleValuedMetric<T>) super.unit(unit);
    }

    public DoubleValuedMetric<T> withMeasurement(T measurement)
    {
        var copy = new DoubleValuedMetric<>(this);
        copy.measurement = measurement;
        return copy;
    }
}

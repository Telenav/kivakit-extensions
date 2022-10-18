package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.kivakit.metrics.core.AggregateMetric;
import com.telenav.kivakit.metrics.core.BaseMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A quantum metric is a single measurement (as opposed to an {@link AggregateMetric}) with a {@link #name()},
 * {@link #type()}, {@link #unit()}, {@link #description()} and {@link #measurement()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
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
    @IncludeProperty
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

package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.metrics.core.BaseMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A double-valued metric
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
@SuppressWarnings("unused")
public class DoubleMetric extends BaseMetric<Double>
{
    private double value;

    public DoubleMetric()
    {
        this(0.0);
    }

    public DoubleMetric(double value)
    {
        this.value = value;
    }

    public DoubleMetric(BaseMetric<Double> that)
    {
        super(that);
        value = that.doubleValue();
    }

    @Override
    public DoubleMetric description(String description)
    {
        return (DoubleMetric) super.description(description);
    }

    @Override
    public double doubleValue()
    {
        return value;
    }

    @Override
    public Double measurement()
    {
        return value;
    }

    @Override
    public DoubleMetric name(String name)
    {
        return (DoubleMetric) super.name(name);
    }

    @Override
    public DoubleMetric type(MetricType type)
    {
        return (DoubleMetric) super.type(type);
    }

    @Override
    public DoubleMetric unit(String unit)
    {
        return (DoubleMetric) super.unit(unit);
    }

    public DoubleMetric withMeasurement(double quantum)
    {
        var copy = new DoubleMetric(this);
        copy.value = quantum;
        return copy;
    }
}

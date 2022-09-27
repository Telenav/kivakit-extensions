package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.metrics.core.BaseMetric;

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

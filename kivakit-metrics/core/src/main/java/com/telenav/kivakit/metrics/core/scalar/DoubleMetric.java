package com.telenav.kivakit.metrics.core.scalar;

import com.telenav.kivakit.metrics.core.BaseMetric;

public class DoubleMetric extends BaseMetric<Double>
{
    private double quantum;

    public DoubleMetric()
    {
        this(0.0);
    }

    public DoubleMetric(double quantum)
    {
        this.quantum = quantum;
    }

    public DoubleMetric(BaseMetric<Double> that)
    {
        super(that);
        quantum = that.doubleQuantum();
    }

    @Override
    public DoubleMetric description(String description)
    {
        return (DoubleMetric) super.description(description);
    }

    @Override
    public double doubleQuantum()
    {
        return quantum;
    }

    @Override
    public Double measurement()
    {
        return quantum;
    }

    @Override
    public DoubleMetric name(String name)
    {
        return (DoubleMetric) super.name(name);
    }

    @Override
    public long quantum()
    {
        return (long) quantum;
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
        copy.quantum = quantum;
        return copy;
    }
}

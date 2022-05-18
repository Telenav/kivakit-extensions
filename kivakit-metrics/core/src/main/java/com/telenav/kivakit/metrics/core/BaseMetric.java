package com.telenav.kivakit.metrics.core;

import com.telenav.kivakit.core.time.Time;

/**
 * Base class for metrics. All metrics have a {@link #createdAt()} time, which is the time at which the metric started
 * collecting data.
 *
 * @author jonathanl (shibo)
 */
public abstract class BaseMetric<T> implements Metric<T>
{
    /** The time at which this metric was created */
    private final Time created = Time.now();

    /** The logical name of the metric */
    private String name;

    /** A human-readable description of the metric */
    private String description;

    /** The unit of the metric */
    private String unit;

    /** The kind of metric */
    private MetricType type;

    public BaseMetric()
    {
    }

    public BaseMetric(BaseMetric<T> that)
    {
        name = that.name;
        description = that.description;
        type = that.type;
        unit = that.unit;
    }

    @Override
    public Time createdAt()
    {
        return created;
    }

    @Override
    public String description()
    {
        return description;
    }

    public BaseMetric<T> description(String description)
    {
        this.description = description;
        return this;
    }

    public BaseMetric<T> name(String name)
    {
        this.name = name;
        return this;
    }

    @Override
    public String name()
    {
        return name;
    }

    public BaseMetric<T> type(MetricType type)
    {
        this.type = type;
        return this;
    }

    @Override
    public MetricType type()
    {
        return type;
    }

    @Override
    public String unit()
    {
        return unit;
    }

    public BaseMetric<T> unit(String unit)
    {
        this.unit = unit;
        return this;
    }
}

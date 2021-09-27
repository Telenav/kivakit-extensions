package com.telenav.kivakit.microservice.rest.microservlet.metrics;

import com.telenav.kivakit.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.microservice.rest.microservlet.internal.metrics.BaseMetric;

public abstract class QuantumMetric<T extends Quantizable> extends BaseMetric<T> implements AggregateMetric<T>
{
    private long total;

    private long maximum;

    private long minimum;

    private int count;

    private final MapFactory<Long, T> factory;

    public QuantumMetric(MapFactory<Long, T> factory)
    {
        this.factory = factory;
    }

    @Override
    public boolean add(final Quantizable quantizable)
    {
        final long quantum = quantizable.quantum();
        total += quantum;
        maximum = Math.max(this.maximum, quantum);
        minimum = Math.min(this.minimum, quantum);
        count++;
        return true;
    }

    @Override
    public final T measurement()
    {
        return factory.newInstance(quantumMeasurement());
    }

    protected int count()
    {
        return count;
    }

    protected long maximum()
    {
        return maximum;
    }

    protected long minimum()
    {
        return maximum;
    }

    protected abstract long quantumMeasurement();

    protected long total()
    {
        return total;
    }
}

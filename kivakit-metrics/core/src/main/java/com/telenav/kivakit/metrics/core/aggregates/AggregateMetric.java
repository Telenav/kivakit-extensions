package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.function.Mapper;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.kivakit.metrics.core.BaseMetric;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.aggregates.count.AverageCountMetric;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.lang.Math.*;

/**
 * A quantum aggregate metric is an {@link com.telenav.kivakit.metrics.core.AggregateMetric} for arbitrary
 * {@link LongValued} objects. The {@link DoubleValued#doubleValue()} ()} method yields each object's quantum value (a
 * double precision floating point number) when it is added to the aggregate with {@link #onAdd(DoubleValued)}.
 * Aggregation maintains these measurements for the quanta that are added:
 *
 * <p><b>Aggregate Metrics</b></p>
 * <ul>
 *     <li>{@link #total()}</li>
 *     <li>{@link #sampleCount()}</li>
 *     <li>{@link #maximumSample()}</li>
 *     <li>{@link #minimumSample()}</li>
 * </ul>
 *
 * <p>
 * When the {@link Metric#measurement()} method is called, the subclass' implementation of {@link #compute()}
 * uses the aggregate metrics to compute a quantum value. For example, the {@link AverageMetric} class
 * implements {@link #compute()} like this:
 * </p>
 *
 * <pre>
 * protected double compute()
 * {
 *     return total() / count();
 * }</pre>
 *
 * <p>
 * As expected, the average of the quanta is the total quanta divided by the number of quanta. The {@link #measurement()}
 * method then uses the {@link Mapper} passed to the constructor to turn the computed quanta (the average value in this case)
 * back into an object of type T. The result of this design is that aggregate metrics are very easy to implement. The
 * full implementation of {@link AverageMetric} is just:
 * </p>
 *
 * <pre>
 * public class AverageMetric&lt;T extends QuantizableMetric&gt; extends AggregateMetric&lt;T&gt;
 * {
 *     public AverageMetric(Mapper&lt;Long, T&gt; factory)
 *     {
 *         super(factory);
 *     }
 *
 *     protected long compute()
 *     {
 *         return total() / count();
 *     }
 * } </pre>
 *
 * <p>
 * {@link AverageMetric} then serves as the base class for a variety of trivially implemented averages. For example,
 * {@link AverageCountMetric} is implemented like this:
 * </p>
 *
 * <pre>
 * public class AverageCountMetric extends AverageMetric&lt;Count&gt;
 * {
 *     public AverageCountMetric()
 *     {
 *         super(Count::count);
 *     }
 * } </pre>
 *
 * <p>
 * Here, the {@link Count#count(long)} factory method is passed to the superclass. This factory method converts the average
 * quantum of the {@link Count} objects added to the {@link AggregateMetric} back into a {@link Count}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class AggregateMetric<T extends DoubleValued> extends BaseMetric<T> implements
        com.telenav.kivakit.metrics.core.AggregateMetric<T>
{
    /** The total of all added samples */
    private double total;

    /** The maximum of all added samples */
    private double maximumSample;

    /** The minimum of all added samples */
    private double minimumSample;

    /** The number of samples */
    private int sampleCount;

    /**
     * Converts from a double value to the type of aggregated metric
     */
    private final Mapper<Double, T> factory;

    protected AggregateMetric(Mapper<Double, T> factory)
    {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double doubleValue()
    {
        return compute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T measurement()
    {
        return factory.map(compute());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAdd(T metric)
    {
        double value = metric.doubleValue();

        total += value;
        maximumSample = max(maximumSample, value);
        minimumSample = min(minimumSample, value);
        sampleCount++;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        // This Addable is not SpaceLimited
        return 0;
    }

    /**
     * Removes the given metric value from this aggregate. The maximum and minimum sample cannot be reversed since it's
     * not known what the previous maximum or minimum were without the sample to be removed.
     *
     * @param metric The metric to remove
     * @return True if it was removed
     */
    public boolean subtract(T metric)
    {
        total -= metric.doubleValue();
        sampleCount--;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    protected abstract double compute();

    /**
     * Returns the maximum of all aggregated samples
     */
    protected double maximumSample()
    {
        return maximumSample;
    }

    /**
     * Returns the minimum of all aggregated samples
     */
    protected double minimumSample()
    {
        return maximumSample;
    }

    /**
     * Returns the sample count for this aggregate
     */
    protected int sampleCount()
    {
        return sampleCount;
    }

    /**
     * Returns the total of all aggregated samples
     */
    protected double total()
    {
        return total;
    }
}

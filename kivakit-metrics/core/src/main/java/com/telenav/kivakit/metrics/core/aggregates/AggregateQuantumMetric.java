package com.telenav.kivakit.metrics.core.aggregates;

import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.factory.MapFactory;
import com.telenav.kivakit.interfaces.value.DoubleValued;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.kivakit.metrics.core.AggregateMetric;
import com.telenav.kivakit.metrics.core.BaseMetric;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.aggregates.count.AverageCountMetric;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A quantum aggregate metric is an {@link AggregateMetric} for arbitrary {@link LongValued} objects. The
 * {@link DoubleValued#doubleValue()} ()} method yields each object's quantum value (a double precision floating point
 * number) when it is added to the aggregate with {@link #onAdd(DoubleValued)}. Aggregation maintains these measurements
 * for the quanta that are added:
 *
 * <p><b>Aggregate Metrics</b></p>
 * <ul>
 *     <li>{@link #total()}</li>
 *     <li>{@link #count()}</li>
 *     <li>{@link #maximum()}</li>
 *     <li>{@link #minimum()}</li>
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
 * method then uses the {@link MapFactory} passed to the constructor to turn the computed quanta (the average value in this case)
 * back into an object of type T. The result of this design is that aggregate metrics are very easy to implement. The
 * full implementation of {@link AverageMetric} is just:
 * </p>
 *
 * <pre>
 * public class AverageMetric&lt;T extends QuantizableMetric&gt; extends AggregateQuantumMetric&lt;T&gt;
 * {
 *     public AverageMetric(MapFactory&lt;Long, T&gt; factory)
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
 * quantum of the {@link Count} objects added to the {@link AggregateQuantumMetric} back into a {@link Count}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public abstract class AggregateQuantumMetric<T extends DoubleValued> extends BaseMetric<T> implements
        AggregateMetric<T>
{
    private double total;

    private double maximum;

    private double minimum;

    private int count;

    private final MapFactory<Double, T> factory;

    public AggregateQuantumMetric(MapFactory<Double, T> factory)
    {
        this.factory = factory;
    }

    @Override
    public double doubleValue()
    {
        return compute();
    }

    @Override
    public final T measurement()
    {
        return factory.newInstance(compute());
    }

    @Override
    public boolean onAdd(T metric)
    {
        double value = metric.doubleValue();

        total += value;
        maximum = Math.max(maximum, value);
        minimum = Math.min(minimum, value);
        count++;

        return true;
    }

    @Override
    public int size()
    {
        // This Addable is not SpaceLimited
        return 0;
    }

    public boolean subtract(T metric)
    {
        double value = metric.doubleValue();

        total -= value;
        maximum = Math.max(maximum, value);
        minimum = Math.min(minimum, value);
        count++;

        return true;
    }

    protected abstract double compute();

    protected int count()
    {
        return count;
    }

    protected double maximum()
    {
        return maximum;
    }

    protected double minimum()
    {
        return maximum;
    }

    protected double total()
    {
        return total;
    }
}

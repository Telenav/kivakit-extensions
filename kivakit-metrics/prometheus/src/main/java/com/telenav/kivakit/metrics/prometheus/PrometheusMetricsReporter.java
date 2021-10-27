package com.telenav.kivakit.metrics.prometheus;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleCollector;
import io.prometheus.client.hotspot.DefaultExports;

import java.util.HashMap;
import java.util.Map;

/**
 * Reports metrics to Prometheus
 *
 * @author jonathanl (shibo)
 */
public class PrometheusMetricsReporter extends BaseComponent implements MetricsReporter
{
    /** Collectors (gauges, counters and histograms) for each metric by name */
    private Map<String, SimpleCollector<?>> collectors = new HashMap<>();

    public PrometheusMetricsReporter(Listener listener)
    {
        DefaultExports.initialize();
        listener.listenTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void report(Metric<?> metric)
    {
        var collector = collectors.get(metric.name());

        switch (metric.type())
        {
            case COUNT:
                count(metric, (Counter) collector);
                break;

            case LEVEL:
                level(metric, (Gauge) collector);
                break;

            case OBSERVATION:
                histogram(metric, (Histogram) collector);
                break;
        }
    }

    private void count(final Metric<?> metric, Counter counter)
    {
        if (counter == null)
        {
            counter = Counter.build()
                    .name(metric.name())
                    .unit(metric.unit())
                    .help(metric.description())
                    .register();

            collectors.put(metric.name(), counter);
        }
        
        counter.inc(metric.quantum());
    }

    private void histogram(final Metric<?> metric, Histogram histogram)
    {
        if (histogram == null)
        {
            histogram = Histogram.build()
                    .name(metric.name())
                    .unit(metric.unit())
                    .help(metric.description())
                    .register();

            collectors.put(metric.name(), histogram);
        }

        histogram.observe(metric.quantum());
    }

    private void level(final Metric<?> metric, Gauge gauge)
    {
        if (gauge == null)
        {
            gauge = Gauge.build()
                    .name(metric.name())
                    .unit(metric.unit())
                    .help(metric.description())
                    .register();

            collectors.put(metric.name(), gauge);
        }

        gauge.set(metric.quantum());
    }
}

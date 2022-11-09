package com.telenav.kivakit.metrics.prometheus;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.metrics.core.Metric;
import com.telenav.kivakit.metrics.core.MetricsReporter;
import telenav.third.party.prometheus.client.Counter;
import telenav.third.party.prometheus.client.Gauge;
import telenav.third.party.prometheus.client.Histogram;
import telenav.third.party.prometheus.client.SimpleCollector;
import telenav.third.party.prometheus.client.hotspot.DefaultExports;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Reports metrics to Prometheus
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class PrometheusMetricsReporter extends BaseComponent implements MetricsReporter
{
    /** Collectors (gauges, counters and histograms) for each metric by name */
    private final Map<String, SimpleCollector<?>> collectors = new HashMap<>();

    /**
     * @param listener The listener to report any problems to
     */
    public PrometheusMetricsReporter(Listener listener)
    {
        DefaultExports.initialize();
        listener.listenTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void report(Metric<?> metric)
    {
        var collector = collectors.get(metric.name());

        switch (metric.type())
        {
            case COUNT -> count(metric, (Counter) collector);
            case LEVEL -> level(metric, (Gauge) collector);
            case OBSERVATION -> histogram(metric, (Histogram) collector);
        }
    }

    private void count(Metric<?> metric, Counter counter)
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

        counter.inc(metric.longValue());
    }

    private void histogram(Metric<?> metric, Histogram histogram)
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

        histogram.observe(metric.doubleValue());
    }

    private void level(Metric<?> metric, Gauge gauge)
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

        gauge.set(metric.longValue());
    }
}

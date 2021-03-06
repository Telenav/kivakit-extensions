package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.AverageDuration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MicroservletRequestStatisticsAggregator extends BaseComponent
{
    private final Map<String, AverageDuration> duration = new ConcurrentHashMap<>();

    private final Frequency every;

    private Time last = Time.now();

    public MicroservletRequestStatisticsAggregator(Frequency every)
    {
        this.every = every;
    }

    public void add(Listener listener, MicroservletRequestHandlingStatistics statistics)
    {
        var path = statistics.path();
        var average = duration.computeIfAbsent(path, ignored -> new AverageDuration());
        average.add(statistics.elapsed());

        synchronized (this)
        {
            if (last.elapsedSince().isGreaterThan(every.cycleLength()))
            {
                var paths = StringList.stringList();
                for (var key : duration.keySet())
                {
                    paths.add("$ => $", key, duration.get(key));
                }
                listener.announce(paths.titledBox("Average Request Duration"));
                last = Time.now();
                duration.clear();
            }
        }
    }
}

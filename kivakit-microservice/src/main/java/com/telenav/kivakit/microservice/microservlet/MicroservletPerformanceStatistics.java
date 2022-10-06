package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.AverageDuration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * Listens to performance messages and prints the average request duration at the specified frequency
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MicroservletPerformanceStatistics
{
    /** Map from mount path to average duration */
    private final Map<String, AverageDuration> duration = new ConcurrentHashMap<>();

    private final Frequency every;

    /** The last time a message performance message was head */
    private Time last = Time.now();

    /**
     * Creates a performance statistics aggregator
     *
     * @param every The frequency at which the average request time should be shown
     */
    public MicroservletPerformanceStatistics(Frequency every)
    {
        this.every = every;
    }

    /**
     * Adds the given performance message to this statistical aggregate
     *
     * @param listener The listener to broadcast to
     * @param performance The performance information to add
     */
    public void add(Listener listener, MicroservletPerformance performance)
    {
        var path = performance.path();
        var average = duration.computeIfAbsent(path, ignored -> new AverageDuration());
        average.add(performance.elapsed());

        synchronized (this)
        {
            if (last.elapsedSince().isGreaterThan(every.cycleLength()))
            {
                var paths = stringList();
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

package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;

/**
 * Statistics about a given request
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRequestHandlingStatistics
{
    private Time end;

    private String path;

    private Time start;

    public Duration elapsed()
    {
        return end.minus(start);
    }

    public void end()
    {
        end = Time.now();
    }

    public void path(String path)
    {
        this.path = path;
    }

    public String path()
    {
        return path;
    }

    public void start()
    {
        start = Time.now();
    }

    public String toString()
    {
        return Strings.format("$ => $", path, elapsed());
    }
}

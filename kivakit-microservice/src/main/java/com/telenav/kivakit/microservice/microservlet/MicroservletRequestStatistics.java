package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.messaging.Message;

/**
 * Statistics about a given request
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRequestStatistics
{
    private Time start;

    private Time end;

    private String path;

    public Duration elapsed()
    {
        return end.minus(start);
    }

    public void end()
    {
        end = Time.now();
    }

    public void path(final String path)
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
        return Message.format("$ => $", path, elapsed());
    }
}

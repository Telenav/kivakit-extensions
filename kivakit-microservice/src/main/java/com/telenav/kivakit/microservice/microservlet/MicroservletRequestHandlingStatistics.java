package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.language.time.Duration;
import com.telenav.kivakit.language.time.Time;
import com.telenav.kivakit.core.messaging.Message;

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
        return Message.format("$ => $", path, elapsed());
    }
}

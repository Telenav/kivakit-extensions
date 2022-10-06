package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Statistics about a given request:
 *
 * <p><b>Request Handling</b></p>
 *
 * <ul>
 *     <li>{@link #start()} - Called when the request starts</li>
 *     <li>{@link #end()} - Called when the request ends</li>
 *     <li>{@link #path(String)} - Sets the mount path for the request</li>
 * </ul>
 *
 * <p><b>Performance</b></p>
 *
 * <ul>
 *     <li>{@link #elapsed()}</li>
 *     <li>{@link #path()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MicroservletPerformance
{
    /** The mount path of the request */
    private String path;

    /** The start time of the request */
    private Time start;

    /** The end time of the request */
    private Time end;

    /**
     * Returns the amount of time this request took
     */
    public Duration elapsed()
    {
        return end.minus(start);
    }

    /**
     * Called when the request ends
     */
    public void end()
    {
        end = Time.now();
    }

    /**
     * Sets the mount path for this request
     *
     * @param path The path
     */
    public void path(String path)
    {
        this.path = path;
    }

    /**
     * Returns the mount path for this request
     */
    public String path()
    {
        return path;
    }

    /**
     * Called when the request starts
     */
    public void start()
    {
        start = Time.now();
    }

    @Override
    public String toString()
    {
        return Strings.format("$ => $", path, elapsed());
    }
}

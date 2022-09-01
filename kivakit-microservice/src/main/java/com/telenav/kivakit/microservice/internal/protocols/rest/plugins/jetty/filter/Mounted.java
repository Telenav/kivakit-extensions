package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandlingStatistics;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.http.RestPath;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;

/**
 * Base class for mounted JARs and micro-servlets.
 *
 * @author jonathanl (shibo)
 */
public class Mounted extends BaseComponent implements RestProblemReportingTrait
{
    private final RestService restService;

    public Mounted(final RestService restService)
    {
        this.restService = restService;
    }

    @Override
    public RestService restService()
    {
        return restService;
    }

    /**
     * Measures the execution of the given request handling code for the given path
     *
     * @param path The path for the request
     * @param code The request handling code
     */
    protected void measure(RestPath path, Runnable code)
    {
        var statistics = new MicroservletRequestHandlingStatistics();
        statistics.start();
        statistics.path(path.key());

        try
        {
            code.run();
        }
        finally
        {
            statistics.end();
            restService.onRequestStatistics(statistics);
        }
    }
}

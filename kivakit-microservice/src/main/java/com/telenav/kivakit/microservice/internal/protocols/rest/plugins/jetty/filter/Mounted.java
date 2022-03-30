package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblemReportingTrait;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandlingStatistics;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;

/**
 * Base class for mounted JARs and microservlets.
 *
 * @author jonathanl (shibo)
 */
public class Mounted extends BaseComponent implements HttpProblemReportingTrait
{
    private final MicroserviceRestService restService;

    public Mounted(final MicroserviceRestService restService)
    {
        this.restService = restService;
    }

    /**
     * Measures the execution of the given request handling code for the given path
     *
     * @param path The path for the request
     * @param code The request handling code
     */
    protected void measure(MicroservletRestPath path, Runnable code)
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

    protected MicroserviceRestService restService()
    {
        return restService;
    }
}

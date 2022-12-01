package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.microservlet.MicroservletPerformance;
import com.telenav.kivakit.microservice.protocols.rest.http.RestPath;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Base class for mounted JARs and micro-servlets.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseMounted extends BaseComponent implements RestProblemReportingTrait
{
    private final RestService restService;

    protected BaseMounted(RestService restService)
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
        var statistics = new MicroservletPerformance();
        statistics.start();
        statistics.path(path.key());

        try
        {
            code.run();
        }
        finally
        {
            statistics.end();
            restService.onRequestPerformance(statistics);
        }
    }
}

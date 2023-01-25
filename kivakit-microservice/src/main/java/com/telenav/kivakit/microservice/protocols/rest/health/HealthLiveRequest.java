package com.telenav.kivakit.microservice.protocols.rest.health;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.network.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Request handler that provides server live-ness.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
@OpenApiType
    (
        """
            type: object
            description: "Request for server health"
                """
    )
public class HealthLiveRequest extends BaseMicroservletRequest
{
    /**
     * Response object for this request
     */
    @OpenApiType
        (
            """
                type: object
                description: "Response to a health liveness request"
                properties:
                  status:
                    type: string
                    description: "Description of status where ALIVE indicates the server is alive"
                    """
        )
    public static class HealthLiveResponse extends BaseMicroservletResponse
    {
        @Expose
        @IncludeProperty
        private final String status;

        private HealthLiveResponse(String status)
        {
            this.status = status;
        }
    }

    @Expose
    private final String ignored = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public MicroservletResponse onRespond()
    {
        // Go through each MountedApi,
        for (var api : require(JettyMicroservletFilter.class).mountedApis())
        {
            // and if the API in the child process is not alive,
            if (!api.isAlive())
            {
                // then this server is not alive,
                var response = new HealthLiveResponse("Forwarded API is failing: " + api.uri());
                response.restResponse().httpStatus(INTERNAL_SERVER_ERROR);
                return response;
            }
        }

        // otherwise, we are alive.
        return listenTo(new HealthLiveResponse("ALIVE"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return HealthLiveResponse.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}

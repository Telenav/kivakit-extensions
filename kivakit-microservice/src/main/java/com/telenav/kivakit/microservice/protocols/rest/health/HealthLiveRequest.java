package com.telenav.kivakit.microservice.protocols.rest.health;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiRequestHandler;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * Request handler that provides server liveness.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection") @OpenApiIncludeType(description = "Request for server liveness")
public class HealthLiveRequest extends BaseMicroservletRequest
{
    /**
     * Response object for this request
     */
    @SuppressWarnings("SpellCheckingInspection")
    @OpenApiIncludeType(description = "Response to a liveness request")
    public static class HealthLiveResponse extends BaseMicroservletResponse
    {
        @Expose
        @SuppressWarnings({ "SpellCheckingInspection", "FieldCanBeLocal" })
        @KivaKitIncludeProperty
        @OpenApiIncludeMember(description = "The server liveness status")
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
    @OpenApiRequestHandler(tags = { "server liveness" })
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
                response.status(SC_INTERNAL_SERVER_ERROR);
                return response;
            }
        }

        // otherwise, we are alive.
        return new HealthLiveResponse("ALIVE");
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
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}

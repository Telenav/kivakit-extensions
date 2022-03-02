package com.telenav.kivakit.microservice.protocols.rest.health;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.language.reflection.ObjectFormatter;
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
 * Request handler that provides server readiness.
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "Request for server readiness")
public class HealthReadyRequest extends BaseMicroservletRequest
{
    /**
     * Response object for this request
     */
    @OpenApiIncludeType(description = "Response to a readiness request")
    public static class HealthReadyResponse extends BaseMicroservletResponse
    {
        @SuppressWarnings("FieldCanBeLocal")
        @Expose
        @KivaKitIncludeProperty
        @OpenApiIncludeMember(description = "The server status")
        private final String status;

        private HealthReadyResponse(String status)
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
    @OpenApiRequestHandler(tags = { "server readiness" })
    public MicroservletResponse onRespond()
    {
        // Go through each MountedApi,
        for (var api : require(JettyMicroservletFilter.class).mountedApis())
        {
            // and if the API in the child process is not alive,
            if (!api.isReady())
            {
                // then this server is not alive,
                var response = new HealthReadyResponse("Forwarded API is failing: " + api.uri());
                response.status(SC_INTERNAL_SERVER_ERROR);
                return response;
            }
        }

        return new HealthReadyResponse("READY");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return HealthReadyResponse.class;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}

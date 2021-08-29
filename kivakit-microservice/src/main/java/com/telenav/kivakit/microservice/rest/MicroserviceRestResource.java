package com.telenav.kivakit.microservice.rest;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.web.jersey.BaseRestResource;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Base class for microservice REST resources.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceRestResource extends BaseRestResource
{
    /**
     * @return The version of this rest service
     */
    @GET
    @Path("version")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation
            (
                    method = "GET",
                    description = "Gets the current version of this REST service"
            )
    public Response onVersion()
    {
        final var kivakit = KivaKit.get();
        final var application = Application.get();

        final String output = Message.format("$ $\nKivaKit $ ($)",
                application.name(), application.version(), kivakit.projectVersion(), kivakit.build());

        return Response.status(200)
                .entity(output)
                .build();
    }
}

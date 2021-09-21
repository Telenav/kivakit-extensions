package com.telenav.kivakit.microservice.rest;

import com.telenav.kivakit.web.jersey.JerseyGsonSerializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MicroserviceJerseyGsonSerializer extends JerseyGsonSerializer<Object>
{
    public MicroserviceJerseyGsonSerializer(final MicroserviceRestApplication application)
    {
        super(application.gsonFactory());
    }
}

package com.telenav.kivakit.microservice.rest;

import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.web.jersey.BaseRestResource;

/**
 * If a REST application is not using the {@link Microservlet} API, this base class can be extended to implement a
 * normal Jersey REST resource.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceRestResource extends BaseRestResource
{
}

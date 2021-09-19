package com.telenav.kivakit.microservice.rest.microservlet.model.methods;

import com.telenav.kivakit.microservice.rest.microservlet.model.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;

/**
 * @author jonathanl (shibo)
 */
public abstract class MicroservletPost extends BaseMicroservletRequest
{
    /**
     * @return The response to this POST request
     */
    public abstract MicroservletResponse onPost();
}

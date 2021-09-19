package com.telenav.kivakit.microservice.rest.microservlet.model.methods;

import com.telenav.kivakit.microservice.rest.microservlet.model.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;

/**
 * @author jonathanl (shibo)
 */
public abstract class MicroservletGet extends BaseMicroservletRequest
{
    /**
     * @return The response to this get request
     */
    public abstract MicroservletResponse onGet();
}

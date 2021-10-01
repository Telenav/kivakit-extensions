package com.telenav.kivakit.microservice.rest.microservlet.requests;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.cycle.BaseMicroservletRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Request handler for REST DELETE requests. Use {@link MicroserviceRestApplication#mount(String, Class)} to install a
 * request handler.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class MicroservletDeleteRequest extends BaseMicroservletRequest
{
    /**
     * @return The response to this delete request
     */
    public abstract MicroservletResponse onDelete();
}
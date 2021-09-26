package com.telenav.kivakit.microservice.rest.microservlet.model.requests;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.model.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
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

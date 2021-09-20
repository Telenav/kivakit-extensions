package com.telenav.kivakit.microservice.rest.microservlet.model.requests;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.model.BaseMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class MicroservletPostRequest extends BaseMicroservletRequest
{
    /**
     * @return The response to this POST request
     */
    public abstract MicroservletResponse onPost();
}

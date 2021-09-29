package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.internal.cycle.BaseMicroservletMessage;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for response implementations.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class MicroservletResponse extends BaseMicroservletMessage
{
    public String toJson()
    {
        return gson().toJson(this);
    }
}

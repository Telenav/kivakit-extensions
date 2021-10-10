package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Creates a {@link MicroservletResponse} when {@link #onRespond()} is called.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletResponder
{
    /**
     * @return The response to this microservlet request
     */
    MicroservletResponse onRespond();
}

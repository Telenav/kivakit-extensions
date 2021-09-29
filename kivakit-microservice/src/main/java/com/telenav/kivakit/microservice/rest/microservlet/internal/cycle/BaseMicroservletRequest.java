package com.telenav.kivakit.microservice.rest.microservlet.internal.cycle;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletRequest extends BaseMicroservletMessage implements MicroservletRequest
{
}

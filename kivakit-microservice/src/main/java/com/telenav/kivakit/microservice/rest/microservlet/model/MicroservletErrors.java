package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.messaging.Message.Status.RESULT_COMPROMISED;

/**
 * A list of error messages
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public class MicroservletErrors extends MessageList
{
    public MicroservletErrors()
    {
        super(message -> message.isWorseThanOrEqualTo(RESULT_COMPROMISED));
    }
}

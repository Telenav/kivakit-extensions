package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.messaging.Message.Status.RESULT_COMPROMISED;

/**
 * A list of error messages used in {@link MicroservletResponse}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@OpenApiIncludeType(
        description = "List of problems, warnings and other error messages in the event of a server failure")
public class MicroservletErrors extends MessageList
{
    @OpenApiIncludeMember(
            description = "Error list",
            example = "[ \"Invalid start time\" ]",
            genericType = String.class)
    private final StringList errors = new StringList();

    public MicroservletErrors()
    {
        super(message -> message.isWorseThanOrEqualTo(RESULT_COMPROMISED));
    }

    public StringList errors()
    {
        return errors;
    }

    @Override
    public void onMessage(final Message message)
    {
        super.onMessage(message);
        errors.add(message.formatted());
    }
}

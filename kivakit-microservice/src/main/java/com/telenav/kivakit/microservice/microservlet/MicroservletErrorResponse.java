package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A list of error messages used in {@link MicroservletResponse} for specific HTTP error codes. See {@link
 * HttpStatus#hasAssociatedErrorMessages()} for details.
 *
 * @author jonathanl (shibo)
 * @see HttpStatus
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@OpenApiIncludeType(
        description = "List of problems, warnings and other error messages in the event of a client or server problem")
public class MicroservletErrorResponse extends BaseMicroservletResponse
{
    @OpenApiIncludeMember(description = "List of errors that occurred")
    private final ObjectList<MicroservletError> errors = new ObjectList<>();

    public ObjectList<MicroservletError> errors()
    {
        return errors;
    }

    public boolean isEmpty()
    {
        return errors.isEmpty();
    }

    @Override
    public boolean isRepeating()
    {
        return false;
    }

    @Override
    public void onMessage(final Message message)
    {
        errors.addIfNotNull(MicroservletError.of(message));
    }

    /**
     * Sends the errors in this object to the given listener as {@link Problem}s.
     *
     * @param listener The listener to send to
     */
    public void send(Listener listener)
    {
        errors.forEach(error -> error.send(listener));
    }

    @Override
    public Validator validator(final ValidationType type)
    {
        return Validator.NULL;
    }
}

package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

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
    @Expose
    private final List<MicroservletError> errors = new ArrayList<>();

    public List<MicroservletError> errors()
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
    public void onMessage(Message message)
    {
        if (message != null)
        {
            errors.add(MicroservletError.of(message));
        }
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
    public Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }
}

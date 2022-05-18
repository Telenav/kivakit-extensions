package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;

/**
 * Describes an error. Any hierarchical error code (per IETF RFC 7807) in an {@link OperationStatusMessage} subclass,
 * such as {@link Problem} or {@link Warning}, is included in the error description with the error type (the KivaKit
 * class) and the error message.
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(
        description = "An error description, including a hierarchical error code, an error type and a message")
public class MicroservletError
{
    public static MicroservletError of(Message message)
    {
        // If we get a status message like a Warning or Problem,
        if (message instanceof OperationStatusMessage && message.isWorseThanOrEqualTo(Warning.class))
        {
            // then add a MicroservletError to the errors list.
            var statusMessage = (OperationStatusMessage) message;
            return new MicroservletError(statusMessage.code(), statusMessage.getClass().getSimpleName(), statusMessage.formatted());
        }
        return null;
    }

    /**
     * A hierarchical error code per IETF RFC 7807. For example, "errors/authentication/incorrect-password".
     */
    @OpenApiIncludeMember(description = "A hierarchical error code per IETF RFC 7807",
                          example = "errors/authentication/my-error",
                          required = false)
    @Expose
    private final String code;

    /**
     * Details of the problem
     */
    @OpenApiIncludeMember(description = "A formatted description of the error",
                          example = "This is a description of the problem that occurred")
    @Expose
    private final String message;

    /**
     * The type of error, such as Problem, Warning or Alert.
     */
    @OpenApiIncludeMember(
            description = "The message type such as Problem or Warning. Used by the microservice REST client and can be ignored",
            example = "Problem")
    @Expose
    private final String type;

    protected MicroservletError(String code, String type, String message)
    {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    /**
     * Sends this error to the given listener as a {@link Message}
     */
    public void send(Listener listener)
    {
        var type = Message.parseMessageName(Listener.consoleListener(), this.type).getClass();
        var message = OperationMessage.newInstance(Listener.consoleListener(), type, this.message, new Object[] {});
        if (message instanceof OperationMessage)
        {
            var statusMessage = (OperationStatusMessage) message;
            statusMessage.code(code);
            listener.receive(message);
        }
    }
}

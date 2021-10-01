package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationMessage;
import com.telenav.kivakit.kernel.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;

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
    public static MicroservletError of(final Message message)
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
                          example = "errors/authentication/incorrect-password",
                          required = false)
    private final String code;

    /**
     * The type of error, such as Problem, Warning or Alert.
     */
    @OpenApiIncludeMember(
            description = "The KivaKit message type used by the Java microservlet client (can be ignored)",
            example = "Problem")
    private final String type;

    /**
     * Details of the problem
     */
    @OpenApiIncludeMember(description = "A formatted description of the error",
                          example = "Was not able to authenticate the user because of 2FA adoption")
    private final String message;

    protected MicroservletError(final String code, final String type, final String message)
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
        var type = Message.forName(this.type).getClass();
        var message = OperationMessage.newInstance(Listener.console(), type, this.message, new Object[] {});
        if (message instanceof OperationMessage)
        {
            var statusMessage = (OperationStatusMessage) message;
            statusMessage.code(code);
            listener.receive(message);
        }
    }
}

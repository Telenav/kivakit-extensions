package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.microservice.protocols.rest.http.HttpProblem;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApi;
import com.telenav.kivakit.network.http.HttpStatus;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.messaging.Message.parseMessageName;
import static com.telenav.kivakit.core.messaging.Messages.newMessage;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.network.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static com.telenav.kivakit.network.http.HttpStatus.OK;

/**
 * Describes an error. Any hierarchical error code (per IETF RFC 7807) in an {@link OperationStatusMessage} subclass,
 * such as {@link Problem} or {@link Warning}, is included in the error description with the error type (the KivaKit
 * class) and the error message.
 *
 * <p><b>Sending</b></p>
 *
 * <ul>
 *     <li>{@link #sendTo(Listener)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #hierarchicalErrorCode()}</li>
 *     <li>{@link #httpStatus()}</li>
 *     <li>{@link #message()}</li>
 *     <li>{@link #type()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused" })
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
@OpenApi
    (
        """
            description: "Error information, including a message, hierarchical error code, error type and HTTP status code"
            properties:
              httpStatus:
                type: integer
                format: int32
                description: "HTTP status code"
              hierarchicalErrorCode:
                type: string
                description: "A hierarchical error code per IETF RFC 7807"
              message:
                type: string
                description: "A formatted description of the error"
              type:
                type: string
                description: "The message type such as Problem or Warning"
            example:
              httpStatus: 401
              hierarchicalErrorCode: "errors/authentication/incorrect-password"
              message: "Invalid password"
              type: "Problem"
            """
    )
public class MicroservletError
{
    /**
     * Convert the given message into a {@link MicroservletError}, if possible
     *
     * @param message The message to convert
     * @return The {@link MicroservletError} for the message, or null if the message isn't an error
     */
    public static MicroservletError microservletError(Message message)
    {
        // If we get a status message like a Glitch, Warning or Problem,
        if (message instanceof OperationStatusMessage statusMessage && message.isWorseThanOrEqualTo(Glitch.class))
        {
            // then add a MicroservletError to the errors list.
            var httpStatus = OK;
            if (message instanceof HttpProblem)
            {
                httpStatus = ((HttpProblem) message).httpStatus();
            }
            if (message.isFailure())
            {
                httpStatus = INTERNAL_SERVER_ERROR;
            }

            return new MicroservletError(httpStatus,
                statusMessage.code(),
                statusMessage.getClass().getSimpleName(),
                statusMessage.formatted());
        }

        return null;
    }

    /** HTTP status code for this error */
    @FormatProperty
    private final transient HttpStatus httpStatus;

    /** Hierarchical error code per IETF RFC 7807 */
    @Expose
    @FormatProperty
    private final String hierarchicalErrorCode;

    /** Details of the problem */
    @Expose
    @FormatProperty
    private final String message;

    /**
     * The type of error, such as Problem, Warning or Alert.
     */
    @Expose
    @FormatProperty
    private final String type;

    /**
     * Creates a microservlet error for the given hierarchical error code, error type, and message
     *
     * @param httpStatus The HTTP status for this error
     * @param hierarchicalErrorCode The hierarchical (IETF RFC 8707) error code
     * @param type The error type, such as Warning or Problem
     * @param message The message
     * @param arguments Any arguments to use when formatting the message
     */
    protected MicroservletError(HttpStatus httpStatus,
                                String hierarchicalErrorCode,
                                String type, String message,
                                Object... arguments)
    {
        this.httpStatus = httpStatus;
        this.hierarchicalErrorCode = hierarchicalErrorCode;
        this.type = type;
        this.message = format(message, arguments);
    }

    /**
     * Returns the RFC 8707 hierarchical error code for this error
     */
    public String hierarchicalErrorCode()
    {
        return hierarchicalErrorCode;
    }

    /**
     * Returns the HTTP status code for this error
     */
    public HttpStatus httpStatus()
    {
        return httpStatus;
    }

    /**
     * Returns the formatted error message
     */
    public String message()
    {
        return message;
    }

    /**
     * Sends this error to the given listener as a {@link Message}
     */
    public void sendTo(Listener listener)
    {
        var type = parseMessageName(consoleListener(), this.type).getClass();
        var message = newMessage(consoleListener(), type, this.message, new Object[] {});
        if (message instanceof OperationMessage)
        {
            var statusMessage = (OperationStatusMessage) message;
            statusMessage.code(hierarchicalErrorCode);
            listener.receive(message);
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Returns the type of error
     */
    public String type()
    {
        return type;
    }
}

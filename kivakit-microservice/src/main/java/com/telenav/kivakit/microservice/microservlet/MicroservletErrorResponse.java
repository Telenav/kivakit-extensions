package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiType;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.microservice.microservlet.MicroservletError.microservletError;
import static com.telenav.kivakit.network.http.HttpStatus.OK;
import static com.telenav.kivakit.validation.Validator.nullValidator;

/**
 * A list of {@link MicroservletError} messages, with a translation to {@link HttpStatus}
 *
 * <p><b>Sending Error Messages</b></p>
 *
 * <ul>
 *     <li>{@link #sendTo(Listener)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #errors()}</li>
 *     <li>{@link #httpStatus()}</li>
 *     <li>{@link #isEmpty()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see HttpStatus
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
@OpenApiType
    (
        """
            description: "Error portion of response"
            properties:
              errors:
                type: array
                description: "List of errors"
                items:
                  type: MicroservletError
                """
    )
public class MicroservletErrorResponse extends BaseMicroservletResponse
{
    /** List of microservlet errors to include in this reponse */
    @Expose
    private final List<MicroservletError> errors = new ArrayList<>();

    /**
     * Returns the errors in this response
     */
    public List<MicroservletError> errors()
    {
        return errors;
    }

    /**
     * Returns the HTTP status code for the first error that represents failure
     */
    public HttpStatus httpStatus()
    {
        for (var error : errors)
        {
            var status = error.httpStatus();
            if (status.isFailure())
            {
                return status;
            }
        }
        return OK;
    }

    /**
     * Returns true if there are no errors in this reponse
     */
    public boolean isEmpty()
    {
        return errors.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRepeating()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message)
    {
        if (message != null)
        {
            var error = microservletError(message);
            if (error != null)
            {
                errors.add(error);
            }
        }
    }

    /**
     * Sends the errors in this object to the given listener as {@link Problem}s.
     *
     * @param listener The listener to send to
     */
    public void sendTo(Listener listener)
    {
        errors.forEach(error -> error.sendTo(listener));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validator validator(ValidationType type)
    {
        return nullValidator();
    }
}

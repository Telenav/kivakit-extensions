package com.telenav.kivakit.microservice.internal.protocols.rest.cycle;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.network.http.HttpStatus;

/**
 * Represents a problem with an associated {@link HttpStatus} code.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class HttpProblem extends Problem
{
    private final HttpStatus httpStatus;

    public HttpProblem(HttpStatus httpStatus, String message, Object... arguments)
    {
        this(httpStatus, null, message, arguments);
    }

    public HttpProblem(HttpStatus httpStatus, Throwable cause, String message, Object... arguments)
    {
        super(cause, message, arguments);
        this.httpStatus = httpStatus;
    }

    public HttpStatus httpStatus()
    {
        return httpStatus;
    }
}

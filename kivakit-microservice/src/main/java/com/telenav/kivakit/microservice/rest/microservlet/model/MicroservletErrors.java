package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.kernel.messaging.listeners.MessageList;

import static com.telenav.kivakit.kernel.messaging.Message.Status.RESULT_COMPROMISED;

/**
 * A list of error messages
 *
 * @author jonathanl (shibo)
 */
public class MicroservletErrors extends MessageList
{
    public MicroservletErrors()
    {
        super(message -> message.isWorseThanOrEqualTo(RESULT_COMPROMISED));
    }
}

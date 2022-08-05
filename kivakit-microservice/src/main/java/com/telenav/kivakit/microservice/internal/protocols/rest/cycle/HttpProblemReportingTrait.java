package com.telenav.kivakit.microservice.internal.protocols.rest.cycle;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroserviceResponse;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.network.http.HttpStatus;

@SuppressWarnings("unused")
public interface HttpProblemReportingTrait
{
    default void okay(String text, Object... arguments)
    {
        status(HttpStatus.OK);
        response().information(text, arguments);
    }

    default Problem problem(HttpStatus httpStatus, String text, Object... arguments)
    {
        return response().problem(httpStatus, text, arguments);
    }

    default Problem problem(HttpStatus httpStatus, Throwable exception, String text, Object... arguments)
    {
        return response().problem(httpStatus, exception, text + ": " + exception.getMessage(), arguments);
    }

    default JettyMicroserviceResponse response()
    {
        return JettyMicroservletRequestCycle.cycle().response();
    }

    default void status(HttpStatus status)
    {
        response().httpStatus(status);
    }
}

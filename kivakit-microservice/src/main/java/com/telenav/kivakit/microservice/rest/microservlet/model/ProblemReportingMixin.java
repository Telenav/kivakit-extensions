package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroserviceResponse;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;

public interface ProblemReportingMixin
{
    default Problem problem(int status, Throwable exception, final String text, final Object... arguments)
    {
        return response().problem(status, exception, text + ": " + exception.getMessage(), arguments);
    }

    default Problem problem(int status, final String text, final Object... arguments)
    {
        return response().problem(status, text, arguments);
    }

    default JettyMicroserviceResponse response()
    {
        return JettyMicroservletRequestCycle.cycle().response();
    }

    default void status(final int status)
    {
        response().status(status);
    }
}

package com.telenav.kivakit.microservice.internal.protocols.rest.cycle;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroserviceResponse;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;

public interface HttpProblemReportingTrait
{
    default Problem problem(int httpStatus, Throwable exception, String text, Object... arguments)
    {
        return response().problem(httpStatus, exception, text + ": " + exception.getMessage(), arguments);
    }

    default Problem problem(int httpStatus, String text, Object... arguments)
    {
        return response().problem(httpStatus, text, arguments);
    }

    default Problem problem(int httpStatus, String code, Throwable exception, String text, Object... arguments)
    {
        var problem = new Problem(exception, text, arguments);
        problem.cause(exception);
        problem.code(code);
        response().status(httpStatus);
        response().receive(problem);
        return problem;
    }

    default Problem problem(int httpStatus, String code, String text, Object... arguments)
    {
        return problem(httpStatus, code, null, text, arguments);
    }

    default JettyMicroserviceResponse response()
    {
        return JettyMicroservletRequestCycle.cycle().response();
    }

    default void status(int status)
    {
        response().status(status);
    }
}

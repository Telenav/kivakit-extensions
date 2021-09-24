package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.google.gson.Gson;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.MetricReportingMixin;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Base class for all microservlet request and response classes.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseMicroservletRequest
 * @see MicroservletResponse
 * @see MicroservletGetRequest
 * @see MicroservletPostRequest
 * @see MicroservletDeleteRequest
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletMessage implements
        ComponentMixin, Validatable, ProblemReportingMixin, MetricReportingMixin
{
    public Gson gson()
    {
        return JettyMicroservletRequestCycle.cycle().gson();
    }

    @Override
    public Validator validator(final ValidationType type)
    {
        return Validator.NULL;
    }
}

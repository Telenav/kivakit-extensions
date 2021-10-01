package com.telenav.kivakit.microservice.rest.microservlet.internal.cycle;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.metrics.MetricReportingTrait;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;
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
public abstract class BaseMicroservletMessage extends BaseComponent implements
        Validatable,
        ProblemReportingTrait,
        MetricReportingTrait
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

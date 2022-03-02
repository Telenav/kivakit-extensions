package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.coredata.validation.Validatable;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblemReportingTrait;
import com.telenav.kivakit.microservice.project.lexakai.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A microservlet request, with a specific response type given by {@link #responseType()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletRequest extends
        Validatable,
        Component,
        HttpProblemReportingTrait,
        MicroservletRequestHandler
{
    /**
     * @return The type of the response for this request
     */
    Class<? extends MicroservletResponse> responseType();
}

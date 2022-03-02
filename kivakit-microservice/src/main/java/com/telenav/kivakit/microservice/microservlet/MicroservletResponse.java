package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblemReportingTrait;
import com.telenav.kivakit.microservice.project.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface for response implementations.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletResponse extends
        Validatable,
        Component,
        HttpProblemReportingTrait
{
    default void prepare()
    {
    }
}

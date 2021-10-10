package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.microservice.microservlet.rest.internal.cycle.ProblemReportingTrait;
import com.telenav.kivakit.microservice.microservlet.rest.internal.metrics.MetricReportingTrait;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
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
        ProblemReportingTrait,
        MetricReportingTrait
{
}

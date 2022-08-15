package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblemReportingTrait;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface for response implementations.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletResponse extends
        Validatable,
        Component,
        HttpProblemReportingTrait
{
    /**
     * Called to prepare the response
     */
    default void onPrepareResponse()
    {
    }

    /**
     * Called when the response is complete, but before it is sent back
     */
    default void onEndResponse()
    {
    }
}

package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletRequest extends Component, Validatable
{
    /**
     * @return The type of the response for this request
     */
    Class<? extends MicroservletResponse> responseType();
}

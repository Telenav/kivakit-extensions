package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletRequest extends BaseComponent implements MicroservletRequest
{
    /**
     * @return The response to this request
     */
    public abstract MicroservletResponse onRespond();

    @Override
    public Validator validator(final ValidationType type)
    {
        return Validator.NULL;
    }
}

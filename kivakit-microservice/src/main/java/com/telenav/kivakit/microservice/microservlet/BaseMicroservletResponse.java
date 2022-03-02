package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.coredata.validation.ValidationType;
import com.telenav.kivakit.coredata.validation.Validator;
import com.telenav.kivakit.microservice.project.lexakai.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for response implementations.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletResponse extends BaseComponent implements MicroservletResponse
{
    @Override
    public Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }
}

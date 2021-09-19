package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.microservice.rest.microservlet.model.methods.MicroservletGet;
import com.telenav.kivakit.microservice.rest.microservlet.model.methods.MicroservletPost;

/**
 * Base class for all microservlet request and response classes.
 *
 * @author jonathanl (shibo)
 * @see BaseMicroservletRequest
 * @see BaseMicroservletResponse
 * @see MicroservletGet
 * @see MicroservletPost
 */
public abstract class BaseMicroservletMessage extends BaseComponent implements Component, Validatable
{
    @Override
    public Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }
}

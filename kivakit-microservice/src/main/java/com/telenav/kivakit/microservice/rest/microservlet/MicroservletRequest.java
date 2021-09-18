package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;

/**
 * @author jonathanl (shibo)
 */
public interface MicroservletRequest extends ComponentMixin, MicroservletResponder, Validatable
{
    /**
     * @return The type of the response for this request
     */
    Class<MicroservletResponse> responseType();

    @Override
    default Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }
}

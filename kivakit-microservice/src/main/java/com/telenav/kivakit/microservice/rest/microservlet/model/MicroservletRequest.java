package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.kernel.data.validation.Validatable;

public interface MicroservletRequest extends Component, Validatable
{
    /**
     * @return The type of the response for this request
     */
    Class<? extends MicroservletResponse> responseType();
}

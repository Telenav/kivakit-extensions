package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * <b>Not public API</b>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletRequest extends Component, Validatable
{
    enum HttpMethod
    {
        GET,
        POST,
        DELETE
    }

    default HttpMethod httpMethod()
    {
        if (this instanceof MicroservletGetRequest)
        {
            return HttpMethod.GET;
        }
        if (this instanceof MicroservletPostRequest)
        {
            return HttpMethod.POST;
        }
        if (this instanceof MicroservletDeleteRequest)
        {
            return HttpMethod.DELETE;
        }
        return fail("Unsupported request subclass: ${class}", getClass());
    }

    /**
     * @return The type of the response for this request
     */
    Class<? extends MicroservletResponse> responseType();
}

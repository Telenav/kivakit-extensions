package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.validation.Validator.nullValidator;

/**
 * Base class for response implementations. Holds a {@link Result} object that listens to any errors (glitches, warnings
 * and problems) broadcast by this response. When {@link #onEndResponse()} is called, these messages are forwarded to
 * {@link RestResponse#receive(Transmittable)}. From there, the messages will be encoded into the response data.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseMicroservletResponse extends BaseComponent implements MicroservletResponse
{
    /** The result object listening to this request */
    private transient Result<?> result;

    /**
     * This constructor can be called in simple cases where the {@link Result} class is not being used.
     */
    public BaseMicroservletResponse()
    {
    }

    /**
     * This constructor can be called if the response is using the {@link Result} class. The result object will capture
     * the output produced during generation of the response. The captured messages will be replayed into the response.
     *
     * @param result The result
     */
    public BaseMicroservletResponse(Result<?> result)
    {
        this.result = result;

        result.listenTo(this);
    }

    /**
     * Propagates any failure messages in the {@link Result} object to the REST response for the current thread
     */
    @Override
    public void onEndResponse()
    {
        // If the response failed,
        if (result != null && result.failed())
        {
            // propagate any messages in the result object to the response
            result.messages().forEach(restResponse()::receive);
        }
    }

    @Override
    public void onPrepareResponse()
    {
    }

    @Override
    public Validator validator(ValidationType type)
    {
        return nullValidator();
    }
}

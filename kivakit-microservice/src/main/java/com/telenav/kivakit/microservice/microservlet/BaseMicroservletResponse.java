package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.microservice.internal.protocols.rest.cycle.HttpProblem;
import com.telenav.kivakit.microservice.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for response implementations.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletResponse extends BaseComponent implements MicroservletResponse
{
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

    @Override
    public void onEndResponse()
    {
        if (result != null)
        {
            // Propagate any messages in the result object to the request cycle response
            for (var message : result.messages())
            {
                response().receive(message);
            }
        }
    }

    @Override
    public Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }
}

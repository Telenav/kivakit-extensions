package com.telenav.kivakit.microservice.protocols.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

/**
 * <p>
 * A generic Amazon Web Service (AWS) Lambda request handler for one or more KivaKit {@link MicroservletRequest}
 * handlers.
 * </p>
 *
 * <p>
 * This request handler can be added by referencing this class when installing a {@link Microservice} as a Lambda jar on
 * AWS:
 * </p>
 *
 * <pre>
 * com.telenav.kivakit.microservice.protocols.lambda.LambdaRequestHandler</pre>
 *
 * <p>
 * This request handler can handle any {@link MicroservletRequest}. Since this is a potential security problem, request
 * classes must be specifically enabled by overriding {@link Microservice#allowedLambdaRequests()}. Only the {@link
 * MicroservletRequest} classes specified by this method can be executed by this Lambda handler.
 * </p>
 *
 * <p><b>Logging</b></p>
 * <p>
 * Note that the KivaKit logging methods are redirected to the AWS logger provided by {@link Context#getLogger()}. This
 * allows existing error reporting in request handlers to be seen on AWS.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class LambdaRequestHandler implements RequestHandler<MicroservletRequest, MicroservletResponse>, ComponentMixin
{
    /**
     * {@inheritDoc}
     */
    @Override
    public MicroservletResponse handleRequest(MicroservletRequest request, Context context)
    {
        // Add a listener so that any messages that the request broadcasts go to the AWS logger,
        request.addListener(message -> context.getLogger().log(message.asString()));

        // If this request is allowed on this microservice,
        if (require(Microservice.class).allowedLambdaRequests().contains(request))
        {
            // then return the response.
            return request.onRequest();
        }
        else
        {
            problem("Not allowed to handle requests of type: $", request.getClass());
            return null;
        }
    }
}
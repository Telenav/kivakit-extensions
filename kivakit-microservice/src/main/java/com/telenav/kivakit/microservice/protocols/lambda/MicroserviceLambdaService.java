package com.telenav.kivakit.microservice.protocols.lambda;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * AWS Lambda protocol service that allows {@link MicroservletRequest}s to be exposed with {@link #mount(String
 * lambdaName, Version lambdaVersion, Class requestType)}.
 *
 * <p><b>AWS Installation</b></p>
 *
 * <p>
 * To install a KivaKit {@link Microservice} JAR on AWS, see {@link LambdaRequestHandler}.
 * </p>
 *
 * <p><b>Security</b></p>
 *
 * <p>
 * Only Lambda functions that have been explicitly exposed with a call to {@link #mount(String, String, Class)} or
 * {@link #mount(String, Version, Class)} in the subclass' {@link #onInitialize()} method can be invoked.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see LambdaRequestHandler
 */
public class MicroserviceLambdaService extends BaseComponent implements Initializable
{
    /** Exposed Lambda functions and their request handlers */
    private final Map<LambdaFunction, Class<? extends MicroservletRequest>> mounted = new HashMap<>();

    /** True while the {@link #onInitialize()} method is running */
    private boolean initializing = false;

    public MicroserviceLambdaService(Microservice<?> microservice)
    {
        register(this);
    }

    @Override
    public void initialize()
    {
        try
        {
            initializing = true;
            onInitialize();
        }
        finally
        {
            initializing = false;
        }
    }

    /**
     * Exposes the given AWS Lambda function by name and version by creating a mapping to the class of the {@link
     * MicroservletRequest} that should handle requests for the Lambda function.
     *
     * @param lambdaName The name of the lambda function
     * @param lambdaVersion The version of the lambda function
     * @param requestType The type of the request handler for the lambda
     */
    public void mount(String lambdaName, Version lambdaVersion, Class<? extends MicroservletRequest> requestType)
    {
        ensureNotNull(lambdaName);
        ensureNotNull(lambdaVersion);

        if (initializing)
        {
            var lambda = new LambdaFunction(lambdaName, lambdaVersion);
            mounted.put(lambda, requestType);
        }
        else
        {
            problem("Lambda functions must be mounted in onInitialize()");
        }
    }

    /**
     * Convenience method
     *
     * @see #mount(String, Version, Class)
     */
    public void mount(String lambdaName, String lambdaVersion, Class<? extends MicroservletRequest> requestType)
    {
        mount(lambdaName, Version.parse(Listener.throwing(), lambdaVersion), requestType);
    }

    /**
     * @return The request handler for the given lambda function, or null if there is none
     */
    Class<? extends MicroservletRequest> requestType(final LambdaFunction lambda)
    {
        return mounted.get(lambda);
    }
}

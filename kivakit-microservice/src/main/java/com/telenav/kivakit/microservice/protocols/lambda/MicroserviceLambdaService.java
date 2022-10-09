package com.telenav.kivakit.microservice.protocols.lambda;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * AWS Lambda protocol service that allows {@link MicroservletRequest}s to be exposed with
 * {@link #mountLambdaRequestHandler(String lambdaName, Version lambdaVersion, Class requestType)}.
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
 * Only Lambda functions that have been explicitly exposed with a call to
 * {@link #mountLambdaRequestHandler(String, String, Class)} or
 * {@link #mountLambdaRequestHandler(String, Version, Class)} in the subclass' {@link #onInitialize()} method can be
 * invoked.
 * </p>
 *
 * <p><b>Mounting Lambda Request Handlers</b></p>
 *
 * <ul>
 *     <li>{@link #mountLambdaRequestHandler(String, String, Class)}</li>
 *     <li>{@link #mountLambdaRequestHandler(String, Version, Class)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see LambdaRequestHandler
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class MicroserviceLambdaService extends BaseComponent implements Initializable
{
    /** True while the {@link #onInitialize()} method is running */
    private boolean initializing = false;

    /** Exposed Lambda functions and their request handlers */
    private final Map<LambdaFunction, Class<? extends MicroservletRequest>> mounted = new HashMap<>();

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
     * Exposes the given AWS Lambda function by name and version by creating a mapping to the class of the
     * {@link MicroservletRequest} that should handle requests for the Lambda function.
     *
     * @param lambdaName The name of the lambda function
     * @param lambdaVersion The version of the lambda function
     * @param requestType The type of the request handler for the lambda
     */
    public void mountLambdaRequestHandler(String lambdaName, Version lambdaVersion,
                                          Class<? extends MicroservletRequest> requestType)
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
     * @see #mountLambdaRequestHandler(String, Version, Class)
     */
    public void mountLambdaRequestHandler(String lambdaName, String lambdaVersion,
                                          Class<? extends MicroservletRequest> requestType)
    {
        mountLambdaRequestHandler(lambdaName, Version.parseVersion(Listener.throwingListener(), lambdaVersion), requestType);
    }

    /**
     * @return The request handler for the given lambda function, or null if there is none
     */
    Class<? extends MicroservletRequest> requestType(LambdaFunction lambda)
    {
        return mounted.get(lambda);
    }
}

package com.telenav.kivakit.microservice.protocols.rest.gson;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiJsonRequest;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Interface that allows a response object to serialize a different object as the JSON response.
 *
 * @author jonathanl (shibo)
 * @see OpenApiJsonRequest
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface MicroserviceGsonObjectSource
{
    /**
     * Returns a gson object
     */
    Object gsonObject();
}

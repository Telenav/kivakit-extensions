package com.telenav.kivakit.microservice.protocols.rest.gson;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiJsonRequest;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Interface that allows a response object to serialize a different object as the JSON response.
 *
 * @author jonathanl (shibo)
 * @see OpenApiJsonRequest
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface MicroserviceYamlSource
{
    /**
     * Returns a gson object
     */
    String yaml();
}

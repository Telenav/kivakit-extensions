package com.telenav.kivakit.microservice.internal.protocols;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 * <p>
 * This interface is implemented by protocol services, like ({@link MicroserviceGrpcService}, that wish to copy all
 * mounted request objects from {@link RestService} by calling the method
 * {@link RestService#mountAllOn(MicroservletMountTarget)}.
 *
 * @author jonathanl (shibo)
 * @see MicroserviceGrpcService
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public interface MicroservletMountTarget
{
    /**
     * Mounts the given request handler on the given path
     *
     * @param path The absolute path
     * @param requestHandlerType The request handler type
     */
    void mount(String path, Class<? extends MicroservletRequestHandler> requestHandlerType);
}

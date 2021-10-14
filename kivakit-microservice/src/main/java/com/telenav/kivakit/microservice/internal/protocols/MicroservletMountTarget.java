package com.telenav.kivakit.microservice.internal.protocols;

import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;

/**
 * <b>Not public API</b>
 * <p>
 * This interface is implemented by protocol services, like ({@link MicroserviceGrpcService}, that wish to copy all
 * mounted request objects from {@link MicroserviceRestService} by calling the method {@link
 * MicroserviceRestService#mountAll(MicroservletMountTarget)}.
 *
 * @author jonathanl (shibo)
 * @see MicroserviceGrpcService
 */
public interface MicroservletMountTarget
{
    /**
     * Mounts the given request handler on the given path
     *
     * @param path The path
     * @param requestHandlerType The request handler type
     */
    void mount(final String path, final Class<? extends MicroservletRequestHandler> requestHandlerType);
}

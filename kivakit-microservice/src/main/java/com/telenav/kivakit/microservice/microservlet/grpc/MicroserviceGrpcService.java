package com.telenav.kivakit.microservice.microservlet.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.microservlet.MicroservletMountTarget;
import com.telenav.kivakit.microservice.internal.microservlet.grpc.MicroservletGrpcResponder;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceRestService;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import static com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook.Order.LAST;

/**
 * GRPC protocol service that allows {@link MicroservletRequestHandler}s to be mounted with {@link #mount(String,
 * Class)} in the {@link #onInitialize()} method (only). The service is created in {@link Microservice#grpService()} ()}
 * automatically started by the microservice framework on startup.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceGrpcService extends BaseComponent implements
        Initializable,
        Startable,
        Stoppable,
        MicroservletMountTarget
{
    /** The microservice that owns this GRPC service */
    private final Microservice microservice;

    /** The GRPC server */
    private Server server;

    /** True while the constructor is running */
    private boolean mountAllowed = false;

    /** The object that responds to GRPC requests */
    private final MicroservletGrpcResponder responder;

    /** True if this service is running */
    private boolean running;

    public MicroserviceGrpcService(Microservice microservice)
    {
        this.microservice = microservice;
        this.responder = new MicroservletGrpcResponder();
    }

    @Override
    public void initialize()
    {
        // Look up any REST service,
        var restFilter = lookup(MicroserviceRestService.class);
        if (restFilter != null)
        {
            // and mount all the request handlers in that service on this service,
            restFilter.mountAll(this);
        }

        mountAllowed = true;
        try
        {
            // then allow the user to add or override request handlers.
            onInitialize();
        }
        finally
        {
            mountAllowed = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Mounts the given request class on the given path.
     *
     * @param path The path to the given microservlet request handler (requestType). If the path is not absolute
     * (doesn't start with a '/'), it is prefixed with: "/api/[major.version].[minor.version]/", where the version is
     * retrieved from {@link Microservice#version()}. For example, the path "users" in microservlet version 3.1 will
     * resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param requestHandlerType The type of the request handler
     */
    @UmlRelation(label = "mounts", referent = Microservlet.class)
    public void mount(final String path, final Class<? extends MicroservletRequestHandler> requestHandlerType)
    {
        // If we're in the onInitialize() method,
        if (mountAllowed)
        {
            // mount the request type,
            responder.mount(path, requestHandlerType);
        }
        else
        {
            // otherwise complain.
            problem("Request handlers must be mounted in onInitialize()");
        }
    }

    /**
     * Starts this GRPC service
     */
    public boolean start()
    {
        information("Starting GRPC server");

        final int port = microservice.settings().grpcPort();

        server = ServerBuilder.forPort(port)
                .addService(new MicroservletGrpcResponder())
                .build();

        if (tryCatch(server::start, "Unable to start server") != null)
        {
            information("Listening on port " + port);
            KivaKitShutdownHook.register(LAST, this::stop);
            running = true;
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(final Duration wait)
    {
        if (server != null)
        {
            server.shutdown();
        }
    }
}
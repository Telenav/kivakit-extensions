package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.protocols.MicroservletMountTarget;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcResponder;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto.Generators;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import static com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook.Order.LAST;

/**
 * GRPC protocol service that simply copies mounted request handlers from {@link MicroserviceRestService}.
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * This service must be created by {@link Microservice#onNewGrpcService()}. Once created, it is automatically
 * initialized and started by the microservice mini-framework on startup.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see MicroserviceRestService
 */
public class MicroserviceGrpcService extends BaseComponent implements
        Initializable,
        Startable,
        Stoppable,
        MicroservletMountTarget
{
    /** The microservice that owns this GRPC service */
    private final Microservice<?> microservice;

    /** The GRPC server */
    private Server server;

    /** True while the {@link #onInitialize()} method is running */
    private boolean initializing = false;

    /** The object that responds to GRPC requests */
    private final MicroservletGrpcResponder responder;

    /** True if this service is running */
    private boolean running;

    public MicroserviceGrpcService(Microservice<?> microservice)
    {
        this.microservice = microservice;
        responder = listenTo(new MicroservletGrpcResponder());
    }

    @Override
    public void initialize()
    {
        try
        {
            initializing = true;

            // Look up any REST service,
            var restService = lookup(MicroserviceRestService.class);
            if (restService != null)
            {
                // and mount all the request handlers in that service on this service,
                restService.mountAll(this);
            }

            // then allow the user to add or override request handlers.
            onInitialize();
        }
        finally
        {
            initializing = false;
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
    public void mount(String path, Class<? extends MicroservletRequestHandler> requestHandlerType)
    {
        // If we're in the onInitialize() method,
        if (initializing)
        {
            // mount the request type,
            responder.mount(path, requestHandlerType);
        }
        else
        {
            // otherwise, complain.
            problem("Request handlers must be mounted in onInitialize()");
        }
    }

    /**
     * Starts this GRPC service
     */
    public boolean start()
    {
        information("Starting GRPC server");

        int port = microservice.settings().grpcPort();

        server = ServerBuilder.forPort(port)
                .addService(responder)
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
    public void stop(Duration wait)
    {
        if (server != null)
        {
            server.shutdownNow();
        }
    }

    /**
     * Write the .proto definitions for all microservlets that have been registered to the output folder
     */
    public void writeProtoFilesTo(Folder folder)
    {
        for (var requestType : responder.requestTypes())
        {
            writeProtoFile(folder.folder("request").mkdirs(), requestType);
            for (var responseType : responder.responseTypes())
            {
                writeProtoFile(folder.folder("response").mkdirs(), responseType);
            }
        }
    }

    /**
     * Saves the given type into the given folder as a .proto file
     */
    private void writeProtoFile(Folder folder, Class<?> type)
    {
        var schema = require(MicroservletGrpcSchemas.class).schemaFor(type);
        var file = folder.mkdirs().file("$.proto", schema.messageName());
        information("Exporting $", file);
        var proto = Generators.newProtoGenerator(schema);
        file.writer().save(proto.generate());
    }
}

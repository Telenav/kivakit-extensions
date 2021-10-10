package com.telenav.kivakit.microservice.microservlet.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.microservice.Microservice;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import static com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook.Order.LAST;

public class MicroserviceGrpcServer extends BaseComponent
{
    private final Microservice microservice;

    private Server server;

    public MicroserviceGrpcServer(Microservice microservice)
    {
        this.microservice = microservice;
    }

    public void start()
    {
        information("Starting GRPC server");

        final int port = microservice.settings().grpcPort();

        server = ServerBuilder.forPort(port)
                .addService(new MicroserviceGrpcResponder())
                .build();

        tryCatch(server::start, "Unable to start server");
        information("Listening on port " + port);
        KivaKitShutdownHook.register(LAST, this::stop);
    }

    public void stop()
    {
        if (server != null)
        {
            server.shutdown();
        }
    }
}

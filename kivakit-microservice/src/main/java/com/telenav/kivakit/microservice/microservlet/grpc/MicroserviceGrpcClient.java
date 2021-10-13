package com.telenav.kivakit.microservice.microservlet.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.internal.microservlet.grpc.MicroservletGrpcResponder;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.Port;
import io.grpc.ManagedChannelBuilder;

/**
 * Client for sending microservlet requests to a microservice via GRPC.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceGrpcClient extends BaseComponent
{
    private final Port port;

    private final Version version;

    /**
     * @param port The GRPC port
     * @param version The version of the service required
     */
    public MicroserviceGrpcClient(Port port, Version version)
    {
        this.port = port;
        this.version = version;
    }

    public MicroservletResponse request(String path, MicroservletRequest request)
    {
        var channel = ManagedChannelBuilder.forAddress(port.host().name(), port.number())
                .usePlaintext()
                .build();
        try
        {
            var responder = new MicroservletGrpcResponder();
            var object = responder.serialize(request.getClass(), request);

            var protobufRequest = MicroservletGrpcRequestProtobuf.newBuilder()
                    .setPath(path)
                    .setObject(object)
                    .build();

            var stub = MicroservletResponderGrpc.newBlockingStub(channel);
            var response = stub.respond(protobufRequest);
            return (MicroservletResponse) responder.deserialize(request.responseType(), response.getObject());
        }
        finally
        {
            channel.shutdown();
        }
    }
}

package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.Port;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * Client for sending microservlet requests to a microservice via GRPC.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceGrpcClient extends BaseComponent
{
    /** Grpc server port to talk to */
    private final Port port;

    /** The protostuff dynamic schemas for doing GRPC */
    private final MicroservletGrpcSchemas schemas = MicroservletGrpcSchemas.get();

    // Create a GRPC channel,
    private final ManagedChannel channel;

    /**
     * @param port The GRPC server port
     */
    public MicroserviceGrpcClient(Port port)
    {
        this.port = port;

        channel = ManagedChannelBuilder.forAddress(port.host().name(), port.number())
                .usePlaintext()
                .build();
    }

    /**
     * Performs a blocking GRPC call to the given path with the given request object
     *
     * @param path The resource path
     * @param request The request object
     * @param responseType The response type
     * @return The response
     */
    public <T extends MicroservletResponse> T request(String path,
                                                      MicroservletRequest request,
                                                      Class<T> responseType)
    {
        ensureEqual(responseType, request.responseType());

        // serialize the request object to protobuf,
        var object = schemas.serialize(request.getClass(), request);

        // pack the path and the request object into an (internal) request protobuf object,
        var protobufRequest = MicroservletGrpcRequestProtobuf.newBuilder()
                .setPath(path)
                .setObject(object)
                .build();

        // get a stub for the channel and use it to get a response,
        var stub = MicroservletResponderGrpc.newBlockingStub(channel);
        var response = stub.respond(protobufRequest);

        return (T) schemas.deserialize(request.responseType(), response.getObject());
    }

    /**
     * Performs a blocking GRPC call to the given path with the given request object
     *
     * @param path The resource path
     * @param request The request object
     * @param responseType The response type
     * @return The response
     */
    public <T extends MicroservletResponse> MicroservletFutureResponse<T> requestFuture(
            String path,
            MicroservletRequest request,
            Class<T> responseType)
    {
        ensureEqual(responseType, request.responseType());

        // Create a GRPC channel,
        var channel = ManagedChannelBuilder.forAddress(port.host().name(), port.number())
                .usePlaintext()
                .build();

        // serialize the request object to protobuf,
        var object = schemas.serialize(request.getClass(), request);

        // pack the path and the request object into an (internal) request protobuf object,
        var protobufRequest = MicroservletGrpcRequestProtobuf.newBuilder()
                .setPath(path)
                .setObject(object)
                .build();

        // get a stub for the channel and use it to get a response,
        var stub = MicroservletResponderGrpc.newFutureStub(channel);
        return new MicroservletFutureResponse<>(stub, protobufRequest, responseType);
    }

    public void stop()
    {
        channel.shutdown();
    }
}

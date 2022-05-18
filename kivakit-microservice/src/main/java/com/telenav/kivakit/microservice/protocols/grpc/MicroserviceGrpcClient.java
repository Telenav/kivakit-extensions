package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.serialization.properties.PropertiesObjectSerializer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;

/**
 * Client for sending microservlet requests to a microservice via GRPC.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
public class MicroserviceGrpcClient extends BaseComponent
{
    // Create a GRPC channel,
    private final ManagedChannel channel;

    /** Grpc server port to talk to */
    private final Port port;

    /** The default version to use for non-absolute paths */
    private final Version version;

    /**
     * @param port The GRPC server port
     * @param version The default version to use for non-absolute request paths (paths not starting with a slash). For
     * example, if version is 1.0, a request path of "testing" would resolve to "/api/1.0/testing".
     */
    public MicroserviceGrpcClient(Port port, Version version)
    {
        this.port = port;
        this.version = version;

        channel = ManagedChannelBuilder.forAddress(port.host().name(), port.number())
                .usePlaintext()
                .build();

        // Register any object serializers
        onRegisterObjectSerializers();

        // and gRPC schemas.
        register(new MicroservletGrpcSchemas(this));
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
        var object = schemas().serialize(request.getClass(), request);

        // pack the path and the request object into an (internal) request protobuf object,
        var protobufRequest = MicroservletGrpcRequestProtobuf.newBuilder()
                .setPath(normalizePath(path))
                .setObject(object)
                .build();

        // get a stub for the channel and use it to get a response,
        var stub = MicroservletResponderGrpc.newBlockingStub(channel);
        var response = stub.respond(protobufRequest);

        return (T) schemas().deserialize(request.responseType(), response.getObject());
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
        var object = schemas().serialize(request.getClass(), request);

        // pack the path and the request object into an (internal) request protobuf object,
        var protobufRequest = MicroservletGrpcRequestProtobuf.newBuilder()
                .setPath(normalizePath(path))
                .setObject(object)
                .build();

        // get a stub for the channel and use it to get a response,
        var stub = MicroservletResponderGrpc.newFutureStub(channel);
        return listenTo(new MicroservletFutureResponse<>(stub, protobufRequest, responseType));
    }

    public void stop()
    {
        channel.shutdown();
    }

    protected void onRegisterObjectSerializers()
    {
        var serializers = new ObjectSerializers();
        serializers.add(Extension.JSON, new GsonObjectSerializer());
        serializers.add(Extension.PROPERTIES, new PropertiesObjectSerializer());
        register(serializers);
    }

    @NotNull
    private String normalizePath(String path)
    {
        // If the path is not absolute,
        if (!path.startsWith("/"))
        {
            // then turn it into /api/[major].[minor]/path
            path = Strings.format("/api/$.$/$", version.major(), version.minor(), path);
        }

        return path;
    }

    private MicroservletGrpcSchemas schemas()
    {
        return require(MicroservletGrpcSchemas.class);
    }
}

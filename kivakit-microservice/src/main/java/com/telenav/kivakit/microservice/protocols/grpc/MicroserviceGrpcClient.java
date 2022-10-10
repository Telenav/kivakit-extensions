package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.serialization.properties.PropertiesObjectSerializer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.resource.Extension.JSON;
import static com.telenav.kivakit.resource.Extension.PROPERTIES;

/**
 * Client for sending microservlet requests to a microservice via GRPC.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>#{@link MicroserviceGrpcClient#MicroserviceGrpcClient(Port, Version)}</li>
 * </ul>
 *
 * <p><b>Making Requests</b></p>
 *
 * <ul>
 *     <li>{@link #request(String, MicroservletRequest, Class)}  }</li>
 *     <li>{@link #requestFuture(String, MicroservletRequest, Class)}</li>
 * </ul>
 *
 * <p><b>Stopping</b></p>
 *
 * <ul>
 *     <li>{@link #stop()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unchecked", "unused" })
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MicroserviceGrpcClient extends BaseComponent
{
    /** The GRPC channel for this client */
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
    public MicroserviceGrpcClient(@NotNull Port port, @NotNull Version version)
    {
        this.port = port;
        this.version = version;

        // Create the GRPC channel,
        channel = ManagedChannelBuilder.forAddress(port.host().name(), port.portNumber())
                .usePlaintext()
                .build();

        // register any object serializers,
        onRegisterObjectSerializers();

        // and register (generic) gRPC schemas.
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
    public <T extends MicroservletResponse> T request(@NotNull String path,
                                                      @NotNull MicroservletRequest request,
                                                      @NotNull Class<T> responseType)
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
            @NotNull String path,
            @NotNull MicroservletRequest request,
            @NotNull Class<T> responseType)
    {
        ensureEqual(responseType, request.responseType());

        // Create a GRPC channel,
        var channel = ManagedChannelBuilder.forAddress(port.host().name(), port.portNumber())
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

    /**
     * Stops this gRPC client
     */
    public void stop()
    {
        channel.shutdown();
    }

    /**
     * Registers JSON and .properties object serializers
     */
    protected void onRegisterObjectSerializers()
    {
        var serializers = new ObjectSerializerRegistry();
        serializers.add(JSON, new GsonObjectSerializer());
        serializers.add(PROPERTIES, new PropertiesObjectSerializer());
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

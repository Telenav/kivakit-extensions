package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc.MicroservletResponderFutureStub;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

import java.util.concurrent.Future;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Gets gRPC object from a {@link Future} stub
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class MicroservletFutureResponse<T extends MicroservletResponse> extends BaseComponent
{
    private final MicroservletResponderFutureStub stub;

    private final MicroservletGrpcRequestProtobuf request;

    private final Class<T> responseType;

    public MicroservletFutureResponse(MicroservletResponderFutureStub stub,
                                      MicroservletGrpcRequestProtobuf request,
                                      Class<T> responseType)
    {
        this.stub = stub;
        this.request = request;
        this.responseType = responseType;
    }

    /**
     * Deserializes and returns the response for a request
     */
    @SuppressWarnings("unchecked")
    public T get()
    {
        try
        {
            var protobuf = stub.respond(request);
            return (T) require(MicroservletGrpcSchemas.class)
                    .deserialize(responseType, protobuf.get().getObject());
        }
        catch (Exception e)
        {
            problem(e, "Unable to get response for request: $", request);
            return null;
        }
    }
}

package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc.MicroservletResponderFutureStub;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

import java.util.concurrent.Future;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_INTERNAL;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Gets gRPC object from a {@link Future} stub
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_INTERNAL)
public class MicroservletFutureResponse<T extends MicroservletResponse> extends BaseComponent
{
    private final MicroservletResponderFutureStub stub;

    private final MicroservletGrpcRequestProtobuf request;

    private final Class<T> responseType;

    @SuppressWarnings("ClassEscapesDefinedScope")
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

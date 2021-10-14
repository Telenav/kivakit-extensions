package com.telenav.kivakit.microservice.protocols.grpc;

import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc.MicroservletResponderFutureStub;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

public class MicroservletFutureResponse<T extends MicroservletResponse>
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

    public T get()
    {
        try
        {
            var protobuf = stub.respond(request);
            return (T) MicroservletGrpcSchemas.get().deserialize(responseType, protobuf.get().getObject());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

package com.telenav.kivakit.microservice.microservlet.grpc;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.protobuf.ByteString;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcResponseProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Class that dispatches GRPC requests to a MicroservletRequest based on the path contained in the {@link
 * MicroservletGrpcRequestProtobuf}.
 *
 * @author jonathanl (shibo)
 */
class MicroserviceGrpcResponder extends MicroservletResponderGrpc.MicroservletResponderImplBase
{
    /** Map from path to request type */
    private final Map<String, Class<?>> pathToRequestType = new ConcurrentHashMap<>();

    /** Cache of request schemas */
    private final Map<Class<?>, Schema<?>> requestTypeToSchema = new ConcurrentHashMap<>();

    /** Cache of response schemas */
    private final Map<Class<?>, Schema<?>> responseTypeToSchema = new ConcurrentHashMap<>();

    public MicroserviceGrpcResponder()
    {
    }

    public void mount(String path, Class<? extends MicroservletRequest> requestType)
    {
        pathToRequestType.put(ensureNotNull(path), requestType);
    }

    @Override
    public void respond(MicroservletGrpcRequestProtobuf requestProtobuf,
                        StreamObserver<MicroservletGrpcResponseProtobuf> responseObserver)
    {
        // Get the request path
        var path = requestProtobuf.getPath();

        // the object type and schema,
        var requestType = ensureNotNull(this.pathToRequestType.get(path));

        // the protobuf serialized byte array,
        var request = (MicroservletRequest) deserialize(requestType, requestProtobuf.getObject());

        // Next call the user's code and get a response, capturing any errors.
        var errors = new MicroservletErrorResponse();
        var response = errors.listenTo(request).onRespond();

        // Turn the MicroservletErrorResponse and MicroservletResponse into a GrpcResponseProtobuf
        var responseProtobuf = MicroservletGrpcResponseProtobuf.newBuilder()
                .addErrors(serialize(MicroservletErrorResponse.class, errors))
                .setObject(serialize(MicroservletResponse.class, response))
                .build();

        // Give the response to the observer
        responseObserver.onNext(responseProtobuf);
        responseObserver.onCompleted();
    }

    /**
     * @return The given bytes deserialized into the given request type
     */
    private Object deserialize(Class<?> type, ByteString bytes)
    {
        var schema = requestTypeToSchema.get(type);
        if (schema == null)
        {
            schema = RuntimeSchema.getSchema(type);
            requestTypeToSchema.put(type, schema);
        }
        final var request = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes.toByteArray(), request, (Schema<Object>) schema);
        return request;
    }

    /**
     * @return The bytes from serializing the given response object
     */
    private ByteString serialize(Class<?> type, Object object)
    {
        var schema = responseTypeToSchema.get(type);
        if (schema == null)
        {
            schema = RuntimeSchema.getSchema((Class<Object>) type);
            responseTypeToSchema.put(type, schema);
        }
        return ByteString.copyFrom(ProtobufIOUtil.toByteArray(object, (Schema<Object>) schema, LinkedBuffer.allocate(4096)));
    }
}

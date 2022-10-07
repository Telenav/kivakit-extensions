package com.telenav.kivakit.microservice.internal.protocols.grpc;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcRequestProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletGrpcResponseProtobuf;
import com.telenav.kivakit.microservice.grpc.MicroservletResponderGrpc;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import io.grpc.stub.StreamObserver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_INTERNAL;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * <b>Not public API</b>
 * <p>
 * Class that dispatches GRPC requests to a MicroservletRequest based on the path contained in the
 * {@link MicroservletGrpcRequestProtobuf}.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_INTERNAL)
public class MicroservletGrpcResponder extends MicroservletResponderGrpc.MicroservletResponderImplBase implements ComponentMixin
{
    /** Map from path to request type */
    private final Map<String, Class<?>> pathToRequestType = new ConcurrentHashMap<>();

    public MicroservletGrpcResponder()
    {
    }

    public void mount(String path, Class<? extends MicroservletRequestHandler> requestHandlerType)
    {
        pathToRequestType.put(ensureNotNull(path), requestHandlerType);
    }

    public Set<Class<?>> requestTypes()
    {
        return new HashSet<>(pathToRequestType.values());
    }

    @Override
    public void respond(MicroservletGrpcRequestProtobuf requestProtobuf,
                        StreamObserver<MicroservletGrpcResponseProtobuf> responseObserver)
    {
        try
        {
            // Get the request path
            var path = requestProtobuf.getPath();

            // the object type and schema,
            var requestType = ensureNotNull(pathToRequestType.get(path));

            // the protobuf serialized byte array,
            var schemas = require(MicroservletGrpcSchemas.class);
            var request = (MicroservletRequest) schemas.deserialize(requestType, requestProtobuf.getObject());

            // Next call the user's code and get a response, capturing any errors.
            var errors = new MicroservletErrorResponse();
            var response = errors.listenTo(request).respond(path);

            // Turn the MicroservletErrorResponse and MicroservletResponse into a GrpcResponseProtobuf
            var responseProtobuf = MicroservletGrpcResponseProtobuf.newBuilder()
                    .addErrors(schemas.serialize(MicroservletErrorResponse.class, errors))
                    .setObject(schemas.serialize(response.getClass(), response))
                    .build();

            // Give the response to the observer
            responseObserver.onNext(responseProtobuf);
            responseObserver.onCompleted();
        }
        catch (Exception e)
        {
            problem(e, "Unable to respond to request");
        }
    }

    public ObjectList<Class<?>> responseTypes()
    {
        var types = new ObjectList<Class<?>>();
        for (var requestType : requestTypes())
        {
            var request = (MicroservletRequest) Type.typeForClass(requestType).newInstance();
            types.add(request.responseType());
        }
        return types;
    }
}

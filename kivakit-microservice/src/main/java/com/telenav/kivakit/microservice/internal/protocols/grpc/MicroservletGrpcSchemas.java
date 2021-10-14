package com.telenav.kivakit.microservice.internal.protocols.grpc;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.protobuf.ByteString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked") public class MicroservletGrpcSchemas
{
    /** Cache of request schemas */
    private static final Map<Class<?>, Schema<?>> requestTypeToSchema = new ConcurrentHashMap<>();

    /** Cache of response schemas */
    private static final Map<Class<?>, Schema<?>> responseTypeToSchema = new ConcurrentHashMap<>();

    public static MicroservletGrpcSchemas get()
    {
        return new MicroservletGrpcSchemas();
    }

    protected MicroservletGrpcSchemas()
    {
    }

    /**
     * @return The given bytes deserialized into the given request type
     */
    public Object deserialize(Class<?> type, ByteString bytes)
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
    public ByteString serialize(Class<?> type, Object object)
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

package com.telenav.kivakit.microservice.internal.protocols.grpc;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.protobuf.ByteString;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dyuproject.protostuff.LinkedBuffer.allocate;
import static com.dyuproject.protostuff.ProtobufIOUtil.toByteArray;
import static com.google.protobuf.ByteString.copyFrom;

/**
 * Allows serialization and deserialization of objects using cached gRPC schemas created at runtime by
 * {@link RuntimeSchema#getSchema(Class)}
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
public class MicroservletGrpcSchemas extends BaseComponent
{
    /** Cache of schemas */
    private static final Map<Class<?>, Schema<?>> typeToSchema = new ConcurrentHashMap<>();

    public MicroservletGrpcSchemas(Listener listener)
    {
        addListener(listener);
        register(this);
    }

    /**
     * @return The given bytes deserialized into the given request type
     */
    public Object deserialize(Class<?> type, ByteString bytes)
    {
        Schema<?> schema = schemaFor(type);
        var request = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes.toByteArray(), request, (Schema<Object>) schema);
        return request;
    }

    /**
     * @return The Protostuff {@link Schema} for the given type
     */
    public Schema<?> schemaFor(Class<?> type)
    {
        var schema = typeToSchema.get(type);
        if (schema == null)
        {
            schema = RuntimeSchema.getSchema(type);
            typeToSchema.put(type, schema);
        }
        return schema;
    }

    /**
     * @return The bytes from serializing the given response object
     */
    public ByteString serialize(Class<?> type, Object object)
    {
        return copyFrom(toByteArray(object, (Schema<Object>) schemaFor(type), allocate(4096)));
    }
}

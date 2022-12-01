package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Collection of .proto file generators
 *
 * @author Alex Shvid
 */

public final class Generators
{

    public static ProtoGenerator newProtoGenerator(Schema<?> schema)
    {
        if (schema instanceof RuntimeSchema)
        {
            return new RuntimeProtoGenerator(schema);
        }
        throw new IllegalArgumentException("unsupported schema type " + schema.getClass());
    }
}

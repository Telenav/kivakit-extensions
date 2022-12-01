package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

/**
 * @author Alex Shvid
 */

public enum RuntimeSchemaType
{

    ArraySchema,
    ObjectSchema,
    MapSchema,
    PolymorphicEnumSchema;

    public static RuntimeSchemaType findByName(String name)
    {
        for (RuntimeSchemaType value : values())
        {
            if (value.name().equals(name))
            {
                return value;
            }
        }
        return null;
    }
}

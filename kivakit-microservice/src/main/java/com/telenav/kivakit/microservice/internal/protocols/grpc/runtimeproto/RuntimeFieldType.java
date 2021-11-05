package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

/**
 * @author Alex Shvid
 */

public enum RuntimeFieldType
{

    RuntimeMessageField,
    RuntimeObjectField,
    RuntimeMapField;

    public static RuntimeFieldType findByName(String name)
    {
        for (RuntimeFieldType value : values())
        {
            if (value.name().equals(name))
            {
                return value;
            }
        }
        return null;
    }
}

package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

import com.dyuproject.protostuff.Tag;

/**
 * @author Alex Shvid
 */

public class MapEntryStringString
{

    @Tag(Constants.ID_MAP_KEY)
    public String key;

    @Tag(Constants.ID_MAP_VALUE)
    public String value;
}

package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

import com.dyuproject.protostuff.Tag;

import java.util.List;

/**
 * @author Alex Shvid
 */

public final class MapStringObject
{

    @Tag(Constants.ID_MAP_ENTRY)
    public List<MapEntryStringObject> value;
}

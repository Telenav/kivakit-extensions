package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

import com.dyuproject.protostuff.Tag;

import java.util.List;

/**
 * @author Alex Shvid
 */

public final class MapObjectObject
{

    @Tag(Constants.ID_MAP_ENTRY)
    public List<MapEntryObjectObject> value;
}

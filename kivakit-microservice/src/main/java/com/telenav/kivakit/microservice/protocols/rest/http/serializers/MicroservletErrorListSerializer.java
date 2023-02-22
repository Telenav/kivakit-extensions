package com.telenav.kivakit.microservice.protocols.rest.http.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.telenav.kivakit.microservice.microservlet.MicroservletError;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorList;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonElementSerializer;

public class MicroservletErrorListSerializer extends BaseGsonElementSerializer<MicroservletErrorList>
{
    public MicroservletErrorListSerializer()
    {
        super(MicroservletErrorList.class);
    }

    @Override
    protected JsonElement toJson(MicroservletErrorList list)
    {
        var array = new JsonArray();
        for (var it : list.errors())
        {
            var element = serialize(it);
            array.add(element);
        }
        return array;
    }

    @Override
    protected MicroservletErrorList toValue(JsonElement object)
    {
        var array = (JsonArray) object;
        var list = new MicroservletErrorList();
        for (var it : array)
        {
            list.add(deserialize(it, MicroservletError.class));
        }
        return list;
    }
}

package com.telenav.kivakit.microservice.methods;

import com.telenav.kivakit.microservice.rest.MicroserviceMethod;

public abstract class MicroserviceGetMethod<Response extends MicroserviceMethod.MicroserviceResponse>
        extends MicroserviceMethod<Response>
{
    @Override
    protected Method method()
    {
        return Method.GET;
    }
}

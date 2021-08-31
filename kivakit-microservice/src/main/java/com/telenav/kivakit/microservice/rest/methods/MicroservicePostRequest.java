package com.telenav.kivakit.microservice.rest.methods;

import com.telenav.kivakit.microservice.rest.MicroserviceRestRequest;

public abstract class MicroservicePostRequest<Response extends MicroserviceRestRequest.MicroserviceResponse>
        extends MicroserviceRestRequest<Response>
{
    @Override
    protected Method method()
    {
        return Method.POST;
    }
}

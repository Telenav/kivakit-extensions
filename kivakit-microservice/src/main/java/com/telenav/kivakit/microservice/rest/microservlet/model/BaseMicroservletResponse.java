package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseMicroservletResponse extends BaseMicroservletMessage implements MicroservletResponse
{
    @JsonProperty
    private final MicroservletErrors errors = new MicroservletErrors();

    public BaseMicroservletResponse()
    {
        errors.listenTo(this);
    }
}

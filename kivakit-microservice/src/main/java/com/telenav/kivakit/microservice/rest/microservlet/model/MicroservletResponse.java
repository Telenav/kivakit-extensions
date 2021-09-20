package com.telenav.kivakit.microservice.rest.microservlet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class MicroservletResponse extends BaseMicroservletMessage
{
    @JsonProperty
    @SuppressWarnings("FieldCanBeLocal")
    @UmlAggregation
    private final MicroservletErrors errors = new MicroservletErrors();

    public MicroservletResponse()
    {
        errors.listenTo(this);
    }
}

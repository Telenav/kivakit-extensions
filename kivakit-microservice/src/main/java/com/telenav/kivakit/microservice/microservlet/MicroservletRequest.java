package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A microservlet request, with a specific response type given by {@link #responseType()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public interface MicroservletRequest extends
        Validatable,
        Component,
        RestProblemReportingTrait,
        MicroservletRequestHandler
{
    /**
     * Returns The type of the response for this request
     */
    Class<? extends MicroservletResponse> responseType();
}

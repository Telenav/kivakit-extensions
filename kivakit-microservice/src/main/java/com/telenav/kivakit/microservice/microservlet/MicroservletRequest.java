package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A microservlet request, with a specific response type given by {@link #responseType()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
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

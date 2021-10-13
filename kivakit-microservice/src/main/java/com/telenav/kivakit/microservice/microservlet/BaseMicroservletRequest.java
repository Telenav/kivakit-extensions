package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for all microservlet requests. A request implements both {@link MicroservletRequest} and {@link
 * MicroservletRequestHandler} because request objects handle themselves. The {@link #version()} method returns The
 * <p>
 * oooo
 * <p>
 * oooo default minimum version required of a server for a request is the version of the microservice compiled against.
 * To communicate with older versions of a microservice, use {@link #version(Version)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletRequest extends BaseComponent implements
        MicroservletRequest,
        MicroservletRequestHandler
{
    /** The minimum major version of the service we require */
    private byte majorVersion;

    /** The minimum minor version of the service we require */
    private byte minorVersion;

    public BaseMicroservletRequest(Version version)
    {
        version(version);
    }

    @Override
    public Validator validator(final ValidationType type)
    {
        return Validator.NULL;
    }

    public MicroservletRequest version(Version version)
    {
        this.majorVersion = (byte) version.major();
        this.minorVersion = (byte) version.minor();
        return this;
    }

    @Override
    public Version version()
    {
        return Version.of(majorVersion, minorVersion);
    }
}

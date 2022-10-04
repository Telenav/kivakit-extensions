package com.telenav.kivakit.microservice;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservice;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Metadata for a microservice
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #description()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #version()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withDescription(String)}</li>
 *     <li>{@link #withName(String)}</li>
 *     <li>{@link #withVersion(Version)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class MicroserviceMetadata
{
    private String name;

    private String description;

    private Version version;

    public MicroserviceMetadata()
    {
    }

    protected MicroserviceMetadata(MicroserviceMetadata that)
    {
        name = that.name;
        description = that.description;
        version = that.version;
    }

    public String description()
    {
        return description;
    }

    public String name()
    {
        return name;
    }

    public Version version()
    {
        return version;
    }

    public MicroserviceMetadata withDescription(String description)
    {
        var copy = copy();
        copy.description = description;
        return copy;
    }

    public MicroserviceMetadata withName(String name)
    {
        var copy = copy();
        copy.name = name;
        return copy;
    }

    public MicroserviceMetadata withVersion(Version version)
    {
        var copy = copy();
        copy.version = version;
        return copy;
    }

    private MicroserviceMetadata copy()
    {
        return new MicroserviceMetadata(this);
    }
}

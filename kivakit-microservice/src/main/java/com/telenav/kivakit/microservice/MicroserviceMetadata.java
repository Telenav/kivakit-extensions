package com.telenav.kivakit.microservice;

import com.telenav.kivakit.language.version.Version;
import com.telenav.kivakit.microservice.project.lexakai.DiagramMicroservice;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramMicroservice.class)
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

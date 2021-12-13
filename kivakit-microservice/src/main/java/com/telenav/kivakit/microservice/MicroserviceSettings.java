package com.telenav.kivakit.microservice;

import com.telenav.kivakit.kernel.data.conversion.string.primitive.BooleanConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramMicroservice.class)
public class MicroserviceSettings
{
    @KivaKitPropertyConverter(IntegerConverter.class)
    private int port;

    @KivaKitPropertyConverter(IntegerConverter.class)
    private int grpcPort;

    @KivaKitPropertyConverter(BooleanConverter.class)
    private boolean server;

    public MicroserviceSettings grpcPort(int grpcPort)
    {
        this.grpcPort = grpcPort;
        return this;
    }

    public int grpcPort()
    {
        return grpcPort;
    }

    public boolean isServer()
    {
        return server;
    }

    public MicroserviceSettings server(final boolean server)
    {
        this.server = server;
        return this;
    }

    public MicroserviceSettings port(int port)
    {
        this.port = port;
        return this;
    }

    public int port()
    {
        return port;
    }
}

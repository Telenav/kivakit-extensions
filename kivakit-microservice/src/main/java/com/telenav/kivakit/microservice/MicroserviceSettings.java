package com.telenav.kivakit.microservice;

import com.telenav.kivakit.conversion.core.language.object.KivaKitConverted;
import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.microservice.lexakai.DiagramMicroservice;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramMicroservice.class)
public class MicroserviceSettings
{
    @KivaKitConverted(IntegerConverter.class)
    private int port;

    @KivaKitConverted(IntegerConverter.class)
    private int grpcPort;

    @KivaKitConverted(BooleanConverter.class)
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

    public MicroserviceSettings port(int port)
    {
        this.port = port;
        return this;
    }

    public int port()
    {
        return port;
    }

    public MicroserviceSettings server(final boolean server)
    {
        this.server = server;
        return this;
    }
}

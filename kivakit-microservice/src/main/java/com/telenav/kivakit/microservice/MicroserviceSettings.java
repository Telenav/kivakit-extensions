package com.telenav.kivakit.microservice;

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

    public void grpcPort(final int grpcPort)
    {
        this.grpcPort = grpcPort;
    }

    public int grpcPort()
    {
        return grpcPort;
    }

    public void port(final int port)
    {
        this.port = port;
    }

    public int port()
    {
        return port;
    }
}

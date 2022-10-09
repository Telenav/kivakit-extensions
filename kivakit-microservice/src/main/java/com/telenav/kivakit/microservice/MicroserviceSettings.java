package com.telenav.kivakit.microservice;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.core.language.object.KivaKitConverted;
import com.telenav.kivakit.conversion.core.language.primitive.BooleanConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservice;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Base microservice settings
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #grpcPort()}</li>
 *     <li>{@link #grpcPort(int)}</li>
 *     <li>{@link #isServer()}</li>
 *     <li>{@link #port()}</li>
 *     <li>{@link #port(int)}</li>
 *     <li>{@link #server(boolean)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
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

    public MicroserviceSettings server(boolean server)
    {
        this.server = server;
        return this;
    }
}

package com.telenav.kivakit.logs.client.network;

import com.telenav.kivakit.network.core.Port;

import java.io.InputStream;

/**
 * @author jonathanl (shibo)
 */
public class Connection
{
    private final Port port;

    private final InputStream input;

    public Connection(final Port port, final InputStream input)
    {
        this.port = port;
        this.input = input;
    }

    public InputStream input()
    {
        return input;
    }

    public Port port()
    {
        return port;
    }
}

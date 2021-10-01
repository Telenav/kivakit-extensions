package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins;

import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest.HttpMethod;
import com.telenav.kivakit.resource.path.FilePath;

import java.util.Objects;

/**
 * A request path and method for a {@link Microservlet}.
 *
 * @author jonathanl (shibo)
 */
public class MicroservletPath implements RegistryTrait
{
    private final FilePath path;

    private final HttpMethod method;

    public MicroservletPath(final FilePath path, final HttpMethod method)
    {
        this.path = path;
        this.method = method;
    }

    public MicroservletPath(final String path, final HttpMethod method)
    {
        this(FilePath.parseFilePath(path), method);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof MicroservletPath)
        {
            var that = (MicroservletPath) object;
            return this.key().equals(that.key());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(key());
    }

    public boolean isNonEmpty()
    {
        return path.isNonEmpty();
    }

    public String key()
    {
        return resolvedPath() + ":" + method.name();
    }

    public HttpMethod method()
    {
        return method;
    }

    public FilePath path()
    {
        return path;
    }

    public FilePath resolvedPath()
    {
        if (!path.startsWith("/"))
        {
            final var version = require(Microservice.class).version();
            var apiPath = Message.format("/api/$.$/$", version.major(), version.minor(), path);
            return FilePath.parseFilePath(apiPath);
        }
        return path;
    }

    @Override
    public String toString()
    {
        return this.path.isEmpty() ? "" : resolvedPath().toString();
    }

    public MicroservletPath withoutLast()
    {
        return new MicroservletPath(path.withoutLast(), method);
    }
}
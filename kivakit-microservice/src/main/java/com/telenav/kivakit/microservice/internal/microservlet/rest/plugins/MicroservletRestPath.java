package com.telenav.kivakit.microservice.internal.microservlet.rest.plugins;

import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.resource.path.FilePath;

import java.util.Objects;

/**
 * A request path and method for a {@link Microservlet}.
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRestPath implements RegistryTrait
{
    private final FilePath path;

    private final HttpMethod httpMethod;

    public MicroservletRestPath(final FilePath path, final HttpMethod httpMethod)
    {
        this.path = path;
        this.httpMethod = httpMethod;
    }

    public MicroservletRestPath(final String path, final HttpMethod httpMethod)
    {
        this(FilePath.parseFilePath(path), httpMethod);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof MicroservletRestPath)
        {
            var that = (MicroservletRestPath) object;
            return this.key().equals(that.key());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(key());
    }

    public HttpMethod httpMethod()
    {
        return httpMethod;
    }

    public boolean isNonEmpty()
    {
        return path.isNonEmpty();
    }

    public String key()
    {
        return resolvedPath() + ":" + httpMethod.name();
    }

    public HttpMethod method()
    {
        return httpMethod;
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

    public MicroservletRestPath withoutLast()
    {
        return new MicroservletRestPath(path.withoutLast(), httpMethod);
    }
}

package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest.HttpMethod;
import com.telenav.kivakit.resource.path.FilePath;

import java.util.Objects;

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
            MicroservletPath that = (MicroservletPath) object;
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
        return path + "/" + method.name();
    }

    public HttpMethod method()
    {
        return method;
    }

    public FilePath path()
    {
        if (!path.startsWith("/"))
        {
            final var version = require(Microservice.class).version();
            var apiPath = "/api/" + version.major() + "." + version.minor() + "/" + path;
            return FilePath.parseFilePath(apiPath);
        }
        return path;
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    public MicroservletPath withoutLast()
    {
        return new MicroservletPath(path.withoutLast(), method);
    }
}

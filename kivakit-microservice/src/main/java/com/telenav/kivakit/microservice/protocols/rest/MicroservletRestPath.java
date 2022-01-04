package com.telenav.kivakit.microservice.protocols.rest;

import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.resource.path.FilePath;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A request path and method for a {@link Microservlet}.
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRestPath implements RegistryTrait, Comparable<MicroservletRestPath>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static MicroservletRestPath parse(Listener listener, String path, HttpMethod httpMethod)
    {
        return new MicroservletRestPath(FilePath.parseFilePath(listener, path), httpMethod);
    }

    private final HttpMethod httpMethod;

    private final FilePath path;

    public MicroservletRestPath(FilePath path, HttpMethod httpMethod)
    {
        this.path = path;
        this.httpMethod = httpMethod;
    }

    public Version version()
    {
        if (path.startsWith("/api/"))
        {
            return Version.parse(LOGGER, path.withoutRoot().get(1));
        }
        return null;
    }

    @Override
    public int compareTo(@NotNull final MicroservletRestPath that)
    {
        return key().compareTo(that.key());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MicroservletRestPath)
        {
            var that = (MicroservletRestPath) object;
            return key().equals(that.key());
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
            var version = require(Microservice.class).version();
            var apiPath = Message.format("/api/$.$/$", version.major(), version.minor(), path);
            return FilePath.parseFilePath(LOGGER, apiPath);
        }
        return path;
    }

    @Override
    public String toString()
    {
        return path.isEmpty() ? "" : resolvedPath().toString();
    }

    public MicroservletRestPath withoutLast()
    {
        return new MicroservletRestPath(path.withoutLast(), httpMethod);
    }
}

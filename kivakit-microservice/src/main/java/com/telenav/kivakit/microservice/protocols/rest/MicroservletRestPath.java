package com.telenav.kivakit.microservice.protocols.rest;

import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.language.strings.Paths;
import com.telenav.kivakit.language.version.Version;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.resource.path.FilePath;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A request path and method for a {@link Microservlet}.
 *
 * @author jonathanl (shibo)
 */
public class MicroservletRestPath implements RegistryTrait, Comparable<MicroservletRestPath>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static final Pattern VERSION_PATTERN = Pattern.compile("/api/(?<version>\\d+\\.\\d+)/");

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

    public MicroservletRestPath(FilePath path)
    {
        this.path = path;
        this.httpMethod = null;
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
            var microservice = require(Microservice.class);
            var version = microservice.version();
            var restService = require(MicroserviceRestService.class);
            var apiPath = restService.versionToPath(version);
            return FilePath.parseFilePath(LOGGER, Paths.concatenate(apiPath, path.asString()));
        }

        return path;
    }

    @Override
    public String toString()
    {
        return path.isEmpty() ? "" : resolvedPath().toString();
    }

    public Version version()
    {
        var matcher = VERSION_PATTERN.matcher(path().asString());
        if (matcher.find())
        {
            return Version.parse(matcher.group("version"));
        }
        return null;
    }

    public MicroservletRestPath withoutLast()
    {
        return new MicroservletRestPath(path.withoutLast(), httpMethod);
    }
}

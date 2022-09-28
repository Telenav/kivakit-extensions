package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.network.http.HttpMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A request path and method for a {@link Microservlet}.
 *
 * @author jonathanl (shibo)
 */
public class RestPath implements
        RegistryTrait,
        Comparable<RestPath>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static final Pattern VERSION_PATTERN = Pattern.compile("/api/(?<version>\\d+\\.\\d+)/");

    public static RestPath parse(Listener listener, String path, HttpMethod httpMethod)
    {
        return new RestPath(FilePath.parseFilePath(listener, path), httpMethod);
    }

    private final HttpMethod httpMethod;

    private final FilePath path;

    public RestPath(FilePath path, HttpMethod httpMethod)
    {
        this.path = path;
        this.httpMethod = ensureNotNull(httpMethod);
    }

    @Override
    public int compareTo(@NotNull final RestPath that)
    {
        return key().compareTo(that.key());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof RestPath)
        {
            var that = (RestPath) object;
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
        return resolvedPath() + httpMethod.name();
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
            var restService = require(RestService.class);
            var apiPath = restService.versionToPath(version);
            return FilePath.parseFilePath(LOGGER, Paths.pathConcatenate(apiPath, path.asString()));
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
            return Version.version(matcher.group("version"));
        }
        return null;
    }

    public RestPath withoutLast()
    {
        return new RestPath(path.withoutLast(), httpMethod);
    }
}

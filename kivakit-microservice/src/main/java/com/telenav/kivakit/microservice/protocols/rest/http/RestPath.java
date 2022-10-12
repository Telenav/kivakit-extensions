package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.network.http.HttpMethod;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Paths.pathConcatenate;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static java.util.Objects.hash;

/**
 * A request path and method for a {@link Microservlet}
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link RestPath#RestPath(FilePath, HttpMethod)}</li>
 *     <li>{@link #parseRestPath(Listener, String, HttpMethod)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #httpMethod()}</li>
 *     <li>{@link #key()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #resolvedPath()}</li>
 *     <li>{@link #version()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class RestPath implements
        RegistryTrait,
        Comparable<RestPath>
{
    /** Pattern for REST paths */
    public static final Pattern API_ROOT_PATTERN = Pattern.compile("/api/(?<version>\\d+\\.\\d+)/");

    /**
     * Returns the given path and HTTP method parsed into a rest path
     *
     * @param listener The listener to notify of problems
     * @param path The path
     * @param httpMethod The HTTP method
     * @return The {@link RestPath}
     */
    public static RestPath parseRestPath(@NotNull Listener listener,
                                         @NotNull String path,
                                         @NotNull HttpMethod httpMethod)
    {
        return new RestPath(parseFilePath(listener, path), httpMethod);
    }

    /** The path */
    private final FilePath path;

    /** The HTTP method */
    private final HttpMethod httpMethod;

    /**
     * Creates a REST path
     *
     * @param path The path
     * @param httpMethod The HTTP method for accessing the path
     */
    public RestPath(@NotNull FilePath path,
                    @NotNull HttpMethod httpMethod)
    {
        this.path = path;
        this.httpMethod = ensureNotNull(httpMethod);
    }

    @Override
    public int compareTo(@NotNull RestPath that)
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
        return hash(key());
    }

    /**
     * Returns the HTTP method for this rest path
     */
    public HttpMethod httpMethod()
    {
        return httpMethod;
    }

    /**
     * Returns true if this path is not empty
     */
    public boolean isNonEmpty()
    {
        return path.isNonEmpty();
    }

    /**
     * Returns this path as a unique key
     */
    public String key()
    {
        return resolvedPath() + httpMethod.name();
    }

    /**
     * Returns the path portion of this rest path
     */
    public FilePath path()
    {
        return path;
    }

    /**
     * Resolves this path by prefixing it with "/api/[version]/"
     *
     * @return The full path
     */
    public FilePath resolvedPath()
    {
        if (!path.startsWith("/"))
        {
            var apiPath = require(RestService.class)
                    .versionToPath(require(Microservice.class).version());

            return parseFilePath(throwingListener(), pathConcatenate(apiPath, path.asString()));
        }

        return path;
    }

    @Override
    public String toString()
    {
        return path.isEmpty() ? "" : resolvedPath().toString();
    }

    /**
     * Returns the version for this path
     */
    public Version version()
    {
        var matcher = API_ROOT_PATTERN.matcher(path().asString());
        if (matcher.find())
        {
            return Version.version(matcher.group("version"));
        }
        return null;
    }

    /**
     * Returns this rest path without the last component of the path portion
     */
    public RestPath withoutLast()
    {
        return new RestPath(path.withoutLast(), httpMethod);
    }
}

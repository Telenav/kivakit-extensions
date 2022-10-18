package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.StringInputStream;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.launcher.JarLauncher;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.http.RestPath;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.resource.Resource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.io.IO.CopyStyle.BUFFERED;
import static com.telenav.kivakit.core.io.IO.copy;
import static com.telenav.kivakit.launcher.JarLauncher.ProcessType.CHILD;
import static com.telenav.kivakit.launcher.JarLauncher.RedirectTo.CONSOLE;
import static com.telenav.kivakit.network.core.LocalHost.localhost;
import static com.telenav.kivakit.network.http.HttpStatus.BAD_REQUEST;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

/**
 * A JAR file mounted on a path. The JAR will be run in a child process on the specified port. Requests will be
 * forwarded by {@link #handleRequest(HttpMethod, JettyRestRequestCycle)} to the child process.
 *
 * <p><b>WARNING</b></p>
 *
 * <p>
 * This API has not been tested and is currently unstable.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MountedApi extends BaseMounted implements TryTrait
{
    /** HTTP client */
    private final HttpClient client;

    /** Command line for application in JAR */
    private StringList commandLine = new StringList();

    /** The JAR */
    private Resource jar;

    /** The path to the JAR */
    private RestPath path;

    /** The port that the JAR is running on */
    private int port;

    /** Any process running the JAR */
    private Process process;

    /** The API version */
    private Version version;

    public MountedApi(RestService service)
    {
        super(service);

        client = HttpClient.newHttpClient();
    }

    public MountedApi commandLine(StringList commandLine)
    {
        this.commandLine = commandLine;
        return this;
    }

    /**
     * Forwards a request to a child process for the given JAR
     *
     * @param method The HTTP method
     * @param cycle The request cycle
     */
    @SuppressWarnings("ClassEscapesDefinedScope")
    public boolean handleRequest(HttpMethod method, JettyRestRequestCycle cycle)
    {
        measure(path, () ->
        {
            maybeLaunch();

            var response = cycle.restResponse().restResponse();
            var uri = URI.create(cycle.restRequest().httpServletRequest().getRequestURI());
            try
            {
                var forwardTo = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(), uri.getFragment());
                var request = HttpRequest.newBuilder(forwardTo);

                switch (method)
                {
                    case GET:
                    {
                        copyResponse(response, tryCatch(() ->
                                        client.send(request.GET().build(), ofString()),
                                "GET request forwarding failed: $", cycle.restRequest().path()));
                    }
                    break;

                    case POST:
                    {
                        var payload = IO.readString(this, cycle.restRequest().httpServletRequest().getInputStream());
                        var postRequest = request.POST(HttpRequest.BodyPublishers.ofString(payload)).build();
                        copyResponse(response, tryCatch(() ->
                                        client.send(postRequest, ofString()),
                                "POST request forwarding failed: $", cycle.restRequest().path()));
                    }
                    break;

                    case DELETE:
                    {
                        copyResponse(response, tryCatch(() ->
                                        client.send(request.DELETE().build(), ofString()),
                                "DELETE request forwarding failed: $", cycle.restRequest().path()));
                    }
                    break;

                    default:
                        unsupported();
                }
            }
            catch (Exception e)
            {
                problem(BAD_REQUEST, "Bad URI: $", uri);
            }
        });

        return true;
    }

    /**
     * Returns true if this mounted API is alive.
     */
    public boolean isAlive()
    {
        var uri = uri("/health/live");
        var location = HttpNetworkLocation.networkLocation(this, uri);
        return "alive".equals(new HttpNetworkLocation(location).get().asString());
    }

    /**
     * Returns true if this mounted API is responding.
     */
    public boolean isReady()
    {
        var uri = uri("/health/ready");
        var location = HttpNetworkLocation.networkLocation(this, uri);
        return "ready".equals(new HttpNetworkLocation(location).get().asString());
    }

    /**
     * Sets the JAR file containing this API
     *
     * @param jar The JAR
     */
    public MountedApi jar(Resource jar)
    {
        this.jar = jar;
        return this;
    }

    public synchronized boolean maybeLaunch()
    {
        if (process == null)
        {
            commandLine.prepend("-port=" + port);
            process = listenTo(new JarLauncher())
                    .arguments(commandLine)
                    .processType(CHILD)
                    .addJarSource(jar)
                    .redirectTo(CONSOLE)
                    .run();
        }
        return process != null;
    }

    /**
     * Sets the path to this API
     *
     * @param path The path
     */
    public MountedApi path(RestPath path)
    {
        this.path = path;
        return this;
    }

    /**
     * Returns the path to this API
     */
    public RestPath path()
    {
        return path;
    }

    /**
     * Sets the local IPC port that this API should be accessed on
     *
     * @param port The port
     */
    public MountedApi port(int port)
    {
        this.port = port;
        return this;
    }

    /**
     * Returns the local port where this API is running
     */
    public Port port()
    {
        return localhost().port(port);
    }

    @Override
    public String toString()
    {
        return Formatter.format("$ ==> $ ($) on port $", path, version, jar, port);
    }

    /**
     * Returns the URI to this API
     */
    public URI uri()
    {
        return uri("");
    }

    /**
     * Returns the URI to the given request handler path in this API
     */
    public URI uri(String path)
    {
        return port().path(this, Paths.pathConcatenate(this.path.toString(), path)).asUri();
    }

    /**
     * Sets the version of this API
     *
     * @param version The API version
     */
    public MountedApi version(Version version)
    {
        this.version = version;
        return this;
    }

    private void copyResponse(RestResponse response, HttpResponse<String> result)
    {
        var httpServletResponse = response.httpServletResponse();
        var map = result.headers().map();
        for (var name : map.keySet())
        {
            httpServletResponse.setHeader(name, map.get(name).get(0));
        }
        httpServletResponse.setStatus(result.statusCode());
        var languages = map.get("Content-Language");
        if (languages.size() > 0)
        {
            var language = languages.get(0);
            var array = language.split("_");
            httpServletResponse.setLocale(new Locale(array[0], array[1]));
        }

        tryCatch(() -> copy(this, new StringInputStream(result.body()), httpServletResponse.getOutputStream(), BUFFERED), "Unable to copy response");
    }
}

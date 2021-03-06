package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.StringInputStream;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.launcher.JarLauncher;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.resource.Resource;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.launcher.JarLauncher.ProcessType.CHILD;
import static com.telenav.kivakit.launcher.JarLauncher.RedirectTo.CONSOLE;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

/**
 * A JAR file mounted on a path. The JAR will be run in a child process on the specified port. Requests will be
 * forwarded by {@link #handleRequest(HttpMethod, JettyMicroservletRequestCycle)} to the child process.
 *
 * @author jonathanl (shibo)
 */
public class MountedApi extends Mounted
{
    /** HTTP client */
    private final HttpClient client;

    /** Command line for application in JAR */
    private StringList commandLine = new StringList();

    /** The JAR */
    private Resource jar;

    /** The path to the JAR */
    private MicroservletRestPath path;

    /** The port that the JAR is running on */
    private int port;

    /** Any process running the JAR */
    private Process process;

    /** The API version */
    private Version version;

    public MountedApi(final MicroserviceRestService service)
    {
        super(service);

        client = HttpClient.newHttpClient();
    }

    public MountedApi commandLine(final StringList commandLine)
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
    public boolean handleRequest(final HttpMethod method, final JettyMicroservletRequestCycle cycle)
    {
        measure(path, () ->
        {
            maybeLaunch();

            var response = cycle.response().httpResponse();
            var uri = URI.create(cycle.request().httpRequest().getRequestURI());
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
                                "GET request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    case POST:
                    {
                        var payload = IO.string(cycle.request().httpRequest().getInputStream());
                        var postRequest = request.POST(HttpRequest.BodyPublishers.ofString(payload)).build();
                        copyResponse(response, tryCatch(() ->
                                        client.send(postRequest, ofString()),
                                "POST request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    case DELETE:
                    {
                        copyResponse(response, tryCatch(() ->
                                        client.send(request.DELETE().build(), ofString()),
                                "DELETE request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    default:
                        unsupported();
                }
            }
            catch (Exception e)
            {
                problem(HttpStatus.INTERNAL_SERVER_ERROR, "Bad URI: $", uri);
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
    public MountedApi jar(final Resource jar)
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
    public MountedApi path(final MicroservletRestPath path)
    {
        this.path = path;
        return this;
    }

    /**
     * Returns the path to this API
     */
    public MicroservletRestPath path()
    {
        return path;
    }

    /**
     * Sets the local IPC port that this API should be accessed on
     *
     * @param port The port
     */
    public MountedApi port(final int port)
    {
        this.port = port;
        return this;
    }

    /**
     * Returns the local port where this API is running
     */
    public Port port()
    {
        return Host.local().port(port);
    }

    public String toString()
    {
        return Strings.format("$ ==> $ ($) on port $", path, version, jar, port);
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
        return port().path(this, Paths.concatenate(this.path.toString(), path)).asUri();
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

    private void copyResponse(HttpServletResponse response, HttpResponse<String> result)
    {
        var map = result.headers().map();
        for (var name : map.keySet())
        {
            response.setHeader(name, map.get(name).get(0));
        }
        response.setStatus(result.statusCode());
        var languages = map.get("Content-Language");
        if (languages.size() > 0)
        {
            var language = languages.get(0);
            var array = language.split("_");
            response.setLocale(new Locale(array[0], array[1]));
        }

        tryCatch(() -> IO.copy(new StringInputStream(result.body()), response.getOutputStream(), IO.CopyStyle.BUFFERED), "Unable to copy response");
    }
}

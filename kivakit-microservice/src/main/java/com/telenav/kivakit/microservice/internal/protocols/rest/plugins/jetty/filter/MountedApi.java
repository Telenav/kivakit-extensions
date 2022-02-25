package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.jar.launcher.JarLauncher;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.resource.resources.jar.launcher.JarLauncher.ProcessType.CHILD;
import static com.telenav.kivakit.resource.resources.jar.launcher.JarLauncher.RedirectTo.CONSOLE;

/**
 * A JAR file mounted on a path. The JAR will be run in a child process on the specified port. Requests will be
 * forwarded by {@link #handleRequest(HttpMethod, JettyMicroservletRequestCycle)} to the child process.
 *
 * @author jonathanl (shibo)
 */
public class MountedApi extends Mounted
{
    /** HTTP client */
    private HttpClient client;

    /** Command line for application in JAR */
    private StringList commandLine = new StringList();

    /** The JAR */
    private Resource jar;

    /** Any path parameters */
    private MicroservletRestPath parameters;

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
    public boolean handleRequest(final HttpMethod method, final JettyMicroservletRequestCycle cycle)
    {
        measure(path, () ->
        {
            maybeLaunch();

            var response = cycle.response().httpResponse();
            var uri = URI.create(cycle.request().httpRequest().getRequestURI());
            try
            {
                var forwardingUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(), uri.getFragment());

                switch (method)
                {
                    case GET:
                    {
                        copyResponse(response, tryCatch(() -> client.execute(new HttpGet(forwardingUri)), "GET request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    case POST:
                    {
                        copyResponse(response, tryCatch(() -> client.execute(new HttpPost(forwardingUri)), "POST request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    case DELETE:
                    {
                        copyResponse(response, tryCatch(() -> client.execute(new HttpDelete(forwardingUri)), "DELETE request forwarding failed: $", cycle.request().path()));
                    }
                    break;

                    default:
                        unsupported();
                }
            }
            catch (Exception e)
            {
                problem(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Bad URI: $", uri);
            }
        });

        return true;
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
     * Sets the parameters to this API
     */
    public void parameters(final MicroservletRestPath parameters)
    {
        this.parameters = parameters;
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

    public String toString()
    {
        return Message.format("$ ==> $ ($) on port $", path, version, jar, port);
    }

    /**
     * Sets the version of this API
     *
     * @param version The API version
     */
    public MountedApi version(final Version version)
    {
        this.version = version;
        return this;
    }

    private void copyResponse(final HttpServletResponse response, final HttpResponse result)
    {
        for (var header : result.getAllHeaders())
        {
            response.setHeader(header.getName(), header.getValue());
        }
        response.setLocale(result.getLocale());
        response.setStatus(result.getStatusLine().getStatusCode());
        tryCatch(() -> IO.copy(result.getEntity().getContent(), response.getOutputStream(), IO.CopyStyle.BUFFERED), "Unable to copy response");
    }
}

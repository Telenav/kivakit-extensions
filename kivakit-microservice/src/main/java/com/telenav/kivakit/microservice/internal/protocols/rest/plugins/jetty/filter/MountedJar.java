package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.io.IO;
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
public class MountedJar extends Mounted
{
    public MountedJar(final MicroserviceRestService service)
    {
        super(service);
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

    private synchronized void maybeLaunch()
    {
        if (process == null)
        {
            commandLine.append("-port=" + port);
            process = listenTo(new JarLauncher())
                    .arguments(commandLine)
                    .processType(CHILD)
                    .addJarSource(jar)
                    .redirectTo(CONSOLE)
                    .run();
        }
    }

    /** The JAR */
    Resource jar;

    /** The port that the JAR is running on */
    int port;

    /** Any process running the JAR */
    Process process;

    /** HTTP client */
    HttpClient client;

    /** The path to the JAR */
    MicroservletRestPath path;

    /** Any path parameters */
    MicroservletRestPath parameters;

    /** Command line for application in JAR */
    public StringList commandLine = new StringList();
}

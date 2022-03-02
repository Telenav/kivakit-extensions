package com.telenav.kivakit.microservice.protocols.rest;

import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.language.strings.Strings;
import com.telenav.kivakit.language.version.Version;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.NetworkAccessConstraints;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.network.http.BaseHttpResource;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.HttpPostResource;
import com.telenav.kivakit.serialization.json.GsonFactory;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

/**
 * A client for easy interaction with KivaKit {@link MicroserviceRestService}s.
 *
 * <p>
 * The constructor of this class takes a {@link Gson} factory to read and write JSON, a {@link Port} specifying the host
 * and port number to communicate with, and a {@link Version} specifying the version of the REST server. The {@link
 * #get(String, Class)} method reads a JSON object of the given type from a path relative to the server specified in the
 * constructor. The {@link #post(String, Class, MicroservletRequest)} method posts the given request object to the given
 * path as JSON and then reads a JSON object response.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceRestClient extends BaseComponent
{
    /** {@link Gson} factory for reading and writing JSON object */
    private final GsonFactory gsonFactory;

    /** The remote host and port number */
    private final Port port;

    /** The version of the remote host's REST service */
    private final Version version;

    /**
     * @param gsonFactory A factory that creates {@link Gson} objects to use when reading and writing JSON objects
     * @param port The (host and) port of the remote REST service to communicate with
     * @param version The version of the remote REST service
     */
    public MicroserviceRestClient(GsonFactory gsonFactory, Port port, Version version)
    {
        this.gsonFactory = gsonFactory;
        this.port = port;
        this.version = version;
    }

    /**
     * Uses HTTP GET to read a JSON object of the given type from the given path. The {@link
     * MicroserviceRestService#gsonFactory()} is used to read the JSON object.
     *
     * @param path The path to the microservlet request handler. If the path is not absolute (doesn't start with a '/'),
     * it is prefixed with: "/api/[major.version].[minor.version]/". For example, the path "users" in microservlet
     * version 3.1 will resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param responseType The type of object to read. Must be a subclass of {@link MicroservletResponse}.
     * @return The response object, or null if a failure occurred
     */
    public <Response extends MicroservletResponse> Response get(String path, Class<Response> responseType)
    {
        return fromJson(new HttpGetResource(networkLocation(path), NetworkAccessConstraints.DEFAULT), responseType);
    }

    /**
     * Convenience method when "posting" a JSON object using path parameters
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    Response post(String path, Class<Response> responseType)
    {
        return post(path, responseType, null);
    }

    /**
     * Uses HTTP POST to write the given object to read a JSON object of the given type from the given path. The {@link
     * MicroserviceRestService#gsonFactory()} is used to read the JSON object.
     *
     * @param path The path to the microservlet request handler. If the path is not absolute (doesn't start with a '/'),
     * it is prefixed with: "/api/[major.version].[minor.version]/". For example, the path "users" in microservlet
     * version 3.1 will resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param responseType The type of object to read. Must be a subclass of {@link MicroservletResponse}.
     * @param request The request object to post as application/json to the given path. If the request is null, then the
     * path or query parameters contains the data to be deserialized into JSON.
     * @return The response object or null if a failure occurred
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    Response post(String path, Class<Response> responseType, Request request)
    {
        var post = listenTo(new HttpPostResource(networkLocation(path), NetworkAccessConstraints.DEFAULT)
        {
            @Override
            protected void onInitialize(HttpPost post)
            {
                try
                {
                    if (request != null)
                    {
                        var entity = new StringEntity(gsonFactory.gson().toJson(request));
                        entity.setContentType("application/json");
                        post.setEntity(entity);
                        post.setHeader("Accept", "application/json");
                        post.setHeader("Content-Type", "application/json");
                    }
                }
                catch (Exception e)
                {
                    problem(e, "Unable to convert object to JSON: $", request);
                }
            }
        });

        return fromJson(post, responseType);
    }

    public Version version()
    {
        return version;
    }

    private <T> T fromJson(BaseHttpResource resource, Class<T> type)
    {
        // Execute the request and read the status code
        var status = resource.status();

        // and if the status is okay,
        if (status.isOkay())
        {
            // then return the JSON payload.
            return readResponse(resource, type);
        }

        // If the status code indicates that there are associated error messages,
        if (status.hasAssociatedErrorMessages())
        {
            // then read the errors,
            var errors = readResponse(resource, MicroservletErrorResponse.class);
            if (errors != null)
            {
                // and send them to listeners of this client.
                errors.send(this);
            }
        }

        return null;
    }

    @NotNull
    private NetworkLocation networkLocation(String path)
    {
        // If the path is not absolute,
        if (!path.startsWith("/"))
        {
            // then turn it into /api/[major].[minor]/path
            path = Message.format("/api/$.$/$", version.major(), version.minor(), path);
        }

        return new NetworkLocation(port.path(this, path));
    }

    private <T> T readResponse(BaseHttpResource resource, Class<T> type)
    {
        if ("application/json".equals(resource.contentType()))
        {
            var json = resource.reader().asString();
            if (!Strings.isEmpty(json))
            {
                return gsonFactory.gson().fromJson(json, type);
            }
            else
            {
                return null;
            }
        }
        else
        {
            problem("Response content type is not application/json, but instead: $", resource.contentType());
            return null;
        }
    }
}

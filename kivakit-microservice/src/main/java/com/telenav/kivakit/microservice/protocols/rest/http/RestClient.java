package com.telenav.kivakit.microservice.protocols.rest.http;

import com.google.gson.JsonSyntaxException;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.core.NetworkLocation;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.network.http.BaseHttpResource;
import com.telenav.kivakit.network.http.HttpGetResource;
import com.telenav.kivakit.network.http.HttpPostResource;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpRequest;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.string.Brackets.bracket;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.network.core.NetworkAccessConstraints.defaultNetworkAccessConstraints;
import static java.lang.Integer.parseInt;

/**
 * A client for easy interaction with KivaKit {@link RestService}s.
 *
 * <p>
 * The constructor of this class takes a {@link RestSerializer} to serialize requests and responses, a {@link Port}
 * specifying the host and port number to communicate with, and a {@link Version} specifying the version of the REST
 * server. The {@link #get(String, Class)} method reads an object of the given type from a path relative to the server
 * specified in the constructor. The {@link #post(String, MicroservletRequest, Class)} method posts the given request
 * object to the given path as text and then reads a response object.
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link RestClient#RestClient(RestSerializer, Port, Version)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #serverVersion()}</li>
 * </ul>
 *
 * <p><b>HTTP Requests</b></p>
 *
 * <ul>
 *     <li>{@link #get(String, Class)}</li>
 *     <li>{@link #post(String, Class)}</li>
 *     <li>{@link #post(String, MicroservletRequest, Class)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class RestClient extends BaseComponent implements TryTrait
{
    public static class PostResponse
    {

    }

    /** Serializer for JSON request serialization and deserialization */
    private final RestSerializer serializer;

    /** The remote host and port number */
    private final Port port;

    /** The version of the remote host's REST service */
    private final Version serverVersion;

    /**
     * @param serializer REST text serialization
     * @param port The (host and) port of the remote REST service to communicate with
     * @param serverVersion The version of the remote REST service
     */
    public RestClient(@NotNull RestSerializer serializer,
                      @NotNull Port port,
                      @NotNull Version serverVersion)
    {
        this.serializer = serializer;
        this.port = port;
        this.serverVersion = serverVersion;
    }

    /**
     * Uses HTTP GET to read an object of the given type from the given path.
     *
     * @param path The path to the microservlet request handler. If the path is not absolute (doesn't start with a '/'),
     * it is prefixed with: "/api/[major.version].[minor.version]/". For example, the path "users" in microservlet
     * version 3.1 will resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param responseType The type of object to read. Must be a subclass of {@link MicroservletResponse}.
     * @return The response object, or null if a failure occurred
     */
    public <Response extends MicroservletResponse> Response get(String path, Class<Response> responseType)
    {
        return readResponse(new HttpGetResource(networkLocation(path), defaultNetworkAccessConstraints()), responseType);
    }

    /**
     * Convenience method when "posting" a JSON object using path parameters
     */
    @SuppressWarnings("unused")
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    Response post(String path, Class<Response> responseType)
    {
        return post(path, null, responseType);
    }

    /**
     * Uses HTTP POST to write the given object to read a JSON object of the given type from the given path.
     *
     * @param path The path to the microservlet request handler. If the path is not absolute (doesn't start with a '/'),
     * it is prefixed with: "/api/[major.version].[minor.version]/". For example, the path "users" in microservlet
     * version 3.1 will resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param request The request object to post as application/json to the given path. If the request is null, then the
     * path or query parameters contains the data to be deserialized into JSON.
     * @param responseType The type of object to read. Must be a subclass of {@link MicroservletResponse}.
     * @return The response object or null if a failure occurred
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    Response post(String path, Request request, Class<Response> responseType)
    {
        return readResponse(postResource(path, request), responseType);
    }

    /**
     * Posts a request and reads a one-JSON-node header from the input before calling the contentReader callback
     * function to allow further input to be read
     *
     * @param path The path to post to
     * @param request The request to post
     * @param responseType The type of the response
     * @return The response object
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    Response postAndReadContent(String path,
                                Request request,
                                Class<Response> responseType)
    {
        var response = postResource(path, request);

        // Get content length in case the content reader wants to show progress,
        Integer length = tryCatchDefault(() -> parseInt(response.responseHeader().get("Content-Length")), null);
        var bytes = length == null ? null : bytes(length);

        // read the response text,
        var text = response.readText();
        try
        {
            // and return the deserialized response
            return serializer.deserialize(text, responseType);
        }
        catch (JsonSyntaxException e)
        {
            problem(e, "Could not parse JSON:\n\n$", text);
        }
        catch (Exception e)
        {
            problem(e, "POST to $ failed", path);
        }
        return null;
    }

    /**
     * Returns the version of the remote server that this client is interacting with. The version is specified during
     * construction.
     */
    public Version serverVersion()
    {
        return serverVersion;
    }

    private <T> T deserializeResponse(BaseHttpResource resource, Class<T> type)
    {
        var contentType = serializer.contentType();
        if (contentType.equals(resource.responseHeader().get("content-type")))
        {
            var text = resource.reader().asString();
            if (contentType.equals("application/json"))
            {
                text = bracket(text);
            }
            if (!isNullOrBlank(text))
            {
                return serializer.deserialize(text, type);
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

    @NotNull
    private NetworkLocation networkLocation(String path)
    {
        // If the path is not absolute,
        if (!path.startsWith("/"))
        {
            // then turn it into /api/[major].[minor]/path
            path = format("/api/$.$/$", serverVersion.major(), serverVersion.minor(), path);
        }

        return new NetworkLocation(port.path(this, path));
    }

    private <Request extends MicroservletRequest> HttpPostResource postResource(String path,
                                                                                Request request)
    {
        return listenTo(new HttpPostResource(networkLocation(path), defaultNetworkAccessConstraints())
        {
            @Override
            public HttpRequest.Builder onInitialize(HttpRequest.Builder builder)
            {
                if (request != null)
                {
                    try
                    {
                        var body = serializer.serialize(request);
                        builder = builder.POST(HttpRequest.BodyPublishers.ofString(body));
                    }
                    catch (Exception e)
                    {
                        problem("Could not post request: $", request);
                    }
                }

                return builder
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json");
            }
        });
    }

    private <T> T readResponse(BaseHttpResource resource, Class<T> type)
    {
        // Execute the request and read the status code
        var status = resource.status();

        // and if the status is okay,
        if (status.isSuccess())
        {
            // then return the JSON payload.
            return deserializeResponse(resource, type);
        }

        // If the status code indicates that there are associated error messages,
        if (status.isFailure())
        {
            // then read the errors,
            var errors = deserializeResponse(resource, MicroservletErrorResponse.class);
            if (errors != null)
            {
                // and send them to listeners of this client.
                errors.sendTo(this);
            }
        }

        return null;
    }
}

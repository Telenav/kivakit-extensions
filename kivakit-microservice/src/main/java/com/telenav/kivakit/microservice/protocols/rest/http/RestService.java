////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.registry.Register;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservice;
import com.telenav.kivakit.microservice.internal.protocols.MicroservletMountTarget;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.MountedApi;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiRequest;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletPerformance;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.serializers.GsonRestSerializer;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Paths.pathConcatenate;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static com.telenav.kivakit.microservice.protocols.rest.http.RestPath.parseRestPath;
import static com.telenav.kivakit.network.http.HttpMethod.GET;
import static com.telenav.kivakit.network.http.HttpMethod.POST;

/**
 * Base class for KivaKit microservice REST applications.
 *
 * <p><b>Mounting Request Handlers</b></p>
 *
 * <p>
 * ({@link MicroservletRequest} handlers must be installed with {@link #mount(Version, String, HttpMethod, Class)}, or
 * {@link #mount(String, HttpMethod, Class)}. All calls to mount request handlers must be made in the
 * {@link Microservice#onInitialize()} method. Attempting to mount a handler outside of
 * {@link Microservice#onInitialize()} will result in a runtime error. For example:
 * </p>
 *
 * <p>
 * In this microservice:
 * </p>
 *
 * <pre>
 * public class MyMicroservice extends Microservice&lt;Void&gt;
 * {
 *     public MicroserviceMetadata metadata()
 *     {
 *         return new MicroserviceMetadata()
 *             .withName("my-microservice")
 *             .withDescription("My microservice")
 *             .withVersion(Version.of(1, 0));
 *     }
 *
 *         [...]
 * }</pre>
 *
 * <p>
 * this call to {@link #mount(Version, String, HttpMethod, Class)} in <i>onInitialize()</i>:
 * </p>
 *
 * <pre>
 * public class MyRestService extends RestService
 * {
 *         [...]
 *
 *     public void onInitialize()
 *     {
 *         var v1 = Version.of(1, 0);
 *
 *         mount(v1, "/users/update", POST, UpdateUserRequest.class);
 *     }
 * }</pre>
 *
 * <p>
 * would mount the POST request handler <i>UserUpdateRequest</i> on the URL
 * <i>/my-microservice/api/1.0/users/update</i>.
 * </p>
 *
 * <p><b>API Forwarding - Backwards Compatibility</b></p>
 *
 * <p>
 * To make it easy to support previous API versions, JAR {@link Resource}s can be launched in a child process using a
 * command-line switch. KivaKit will redirect requests for the previous version to the child process on a specific port
 * on the local host. This ensures full compatibility with the old API with a minimum of effort. This can be
 * accomplished by simply including one or more JAR files in a package inside the microservice JAR (or in a folder
 * somewhere), and then using the <i>-api-forwarding</i> command line switch:
 *
 * <p>
 * <i>-api-forwarding=[version],[jar],[port],[command-line];[...]</i>.
 * </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>-api-forwarding=version=0.9,jar=classpath:/apis/my-microservice-0.9.jar,port=8082,command-line=-deployment=development</pre>
 *
 * <p>
 * Would mount the 0.9 API JAR in the package <i>apis</i> on port 8082, and the 1.0 API JAR in the package <i>apis</i>
 * on port 8083. Any {@link ResourceIdentifier} can be used to specify the JAR resource. The example here uses the
 * <i>classpath</i> scheme, so the JAR will be located on the classpath. See {@link ResourceIdentifier} for details
 * regarding available resource resolvers and their associated schemes.
 * </p>
 *
 * <p>
 * If more control is desired, {@link #mountApi(Version, Resource, String, int)} can be used to manually mount the API
 * JAR for a previous version of the API on the given port number on the local host.
 * </p>
 *
 * <p><b>API Paths and Versions</b></p>
 *
 * <p>
 * The path to a particular API version can be customized by overriding {@link #versionToPath(Version)}, and
 * {@link #pathToVersion(String)} (both must be overridden). By default this format used by both methods is
 * <i>/api/[major-version].[minor-version]</i>. For example, <i>/api/1.0</i>.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Microservice
 * @see MicroservletRequest
 * @see RestPath
 * @see MicroservletMountTarget
 * @see HttpMethod
 * @see Resource
 * @see Version
 */
@SuppressWarnings({ "RedundantSuppression", "unused", "unchecked", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramMicroservice.class)
@Register
public abstract class RestService extends BaseComponent implements Initializable
{
    /** True while initializing */
    private boolean initializing = false;

    /** The microservice that owns this REST service */
    @UmlAggregation
    private final Microservice<?> microservice;

    /** Map from REST path to request handler */
    private final Map<RestPath, Class<? extends MicroservletRequest>> pathToRequest = new HashMap<>();

    private final Lazy<RestSerializer<?, ?>> serializer = lazy(GsonRestSerializer::new);

    /**
     * @param microservice The microservice that is creating this REST service
     */
    protected RestService(Microservice<?> microservice)
    {
        this.microservice = microservice;
        microservice.listenTo(this);

        register(this);
    }

    /**
     * Returns the API version for this REST service (it may also support earlier API versions).
     *
     * @return The API version
     */
    public abstract Version apiVersion();

    /**
     * Mount OpenAPI request handler and initialize the rest service. This method cannot be overridden. Override
     * {@link #onInitialize()} instead.
     */
    @Override
    public final void initialize()
    {
        initializing = true;
        try
        {
            mount("/docs/open-api/swagger.json", GET, OpenApiRequest.class);
            mount("/api/" + apiVersion() + "/docs/open-api/swagger.json", GET, OpenApiRequest.class);

            onInitialize();
        }
        finally
        {
            initializing = false;
        }
    }

    /**
     * Returns the microservice to which this rest service belongs
     */
    public Microservice<?> microservice()
    {
        return microservice;
    }

    /**
     * Mounts the given microservlet on the given path
     *
     * @param path The path to the microservlet. If the path is not absolute (doesn't start with a '/'), it is prefixed
     * with: "/api/[major.version].[minor.version]/". For example, the path "users" in microservlet version 3.1 will
     * resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param microservlet The microservlet to mount
     */
    @UmlRelation(label = "mounts", referent = Microservlet.class)
    public void mount(RestPath path, Microservlet<?, ?> microservlet)
    {
        // If we're in onInitialize(),
        if (initializing)
        {
            // mount the microservlet,
            require(JettyMicroservletFilter.class).mount(path, microservlet);
        }
        else
        {
            // otherwise, complain.
            problem("Request handlers must be mounted in onInitialize()");
        }
    }

    /**
     * Mounts the given request handler on the path /api/[version]/path
     *
     * @param version The version of the request handler
     * @param path The path to mount on
     * @param method The HTTP method
     * @param requestType The type of the request handler
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mount(Version version, String path, HttpMethod method, Class<Request> requestType)
    {
        mount(pathConcatenate(versionToPath(version), path), method, requestType);
    }

    /**
     * Mounts the given request class on the given path.
     *
     * @param path The path to the given microservlet request handler (requestType). If the path is not absolute
     * (doesn't start with a '/'), it is prefixed with: "/api/[major.version].[minor.version]/", where the version is
     * retrieved from {@link Microservice#version()}. For example, the path "users" in microservlet version 3.1 will
     * resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param method The HTTP method to which the microservlet should respond
     * @param requestType The type of the request
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mount(String path, HttpMethod method, Class<Request> requestType)
    {
        // If we're in onInitialize(),
        if (initializing)
        {
            // create a request object, so we can get the response type and HTTP method,
            var request = listenTo(typeForClass(requestType).newInstance());
            if (request != null)
            {
                // then mount an anonymous microservlet on the given path,
                @SuppressWarnings("unchecked")
                var responseType = (Class<Response>) request.responseType();
                ensureNotNull(responseType, "Request type ${class} has no response type", requestType);
                var restPath = parseRestPath(this, pathConcatenate(rootPath(), path), method);
                mount(restPath, listenTo(new Microservlet<Request, Response>(requestType, responseType)
                {
                    @Override
                    public String description()
                    {
                        return format("Handles ${class} requests", requestType());
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public Response onRespond(Request request)
                    {
                        return (Response) request.respond(path);
                    }
                }));

                pathToRequest.put(restPath, requestType);
            }
            else
            {
                problem("Could not create request object: ${class}", requestType);
            }
        }
        else
        {
            problem("Request handlers must be mounted in onInitialize()");
        }
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Mounts all paths that have been mounted on this REST service on the given mount target.
     */
    @SuppressWarnings("ClassEscapesDefinedScope")
    public void mountAllOn(MicroservletMountTarget target)
    {
        for (var path : pathToRequest.keySet())
        {
            target.mount(pathConcatenate(rootPath(), path.resolvedPath().asString()), pathToRequest.get(path));
        }
    }

    /**
     * Mounts the given JAR on the path specified by {@link #versionToPath(Version)}. Requests to the path will be
     * directed to a child process running the JAR. This permits greater agility with strong backwards compatibility (at
     * the expense of some memory and compute resources).
     *
     * @param version The API version in the JAR file
     * @param jar The JAR file to launch in a child process
     * @param commandLine The command line to use when executing the JAR
     * @param port The port to talk to the child process
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mountApi(Version version, Resource jar, String commandLine, int port)
    {
        mountApi(version, versionToPath(version), jar, commandLine, port);
    }

    /**
     * Mounts the given JAR on the given path for the given HTTP method. Requests to the path will be directed to a
     * child process running the JAR. This permits greater agility with strong backwards compatibility (at the expense
     * of some memory and compute resources).
     *
     * @param version The API version in the JAR file
     * @param path The path to the given JAR. If the path is not absolute (doesn't start with a '/'), it is prefixed
     * with: "/api/[major.version].[minor.version]/", where the version is retrieved from
     * {@link Microservice#version()}. For example, the path "users" in microservlet version 3.1 will resolve to
     * "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param jar The JAR file to delegate to
     * @param commandLine The command line to use when executing the JAR
     * @param port The port to talk to the child process
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mountApi(Version version, String path, Resource jar, String commandLine, int port)
    {
        // If we're in onInitialize(),
        if (initializing)
        {
            // get the path to this API,
            var restPath = parseRestPath(this, path, POST);

            // then populate the API descriptor,
            var api = new MountedApi(this);
            api.version(version);
            api.path(restPath);
            api.jar(jar);
            api.commandLine(parseCommandLine(commandLine));
            api.port(port);

            // and mount it.
            require(JettyMicroservletFilter.class).mount(api);
        }
        else
        {
            // otherwise, complain.
            problem("JAR files must be mounted in onInitialize()");
        }
    }

    public void onInitialize(JettyServer server)
    {
    }

    /**
     * Called when OpenAPI YAML is being created to allow additional types to be inspected
     *
     * @return The classes that should be added to the OpenAPI schemas list
     */
    public ObjectList<Class<?>> onOpenApiSchemas()
    {
        return list();
    }

    /**
     * Called to give statistics for each request
     *
     * @param performance The statistics
     */
    public void onRequestPerformance(MicroservletPerformance performance)
    {
    }

    /**
     * Called for every request to this rest service after producing a response but before the response is closed.
     * Overriding this method can be used to add response header(s) to all responses.
     */
    public void onRequested(MicroservletRequest request, HttpMethod method)
    {
    }

    /**
     * Called for every request to this rest service, before activity begins on producing a response
     */
    public void onRequesting(MicroservletRequest request, HttpMethod method)
    {
    }

    /**
     * Returns the serializer to use for this rest service
     *
     * @return The serializer
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    RestSerializer<Request, Response> restSerializer()
    {
        return (RestSerializer<Request, Response>) serializer.get();
    }

    /**
     * Extracts any version from the given path, which must be in the format produced by
     * {@link #versionToPath(Version)}.
     *
     * @param path The path containing version information
     * @return The version
     */
    protected Version pathToVersion(String path)
    {
        var matcher = Pattern.compile("/api/(<?version>[^/]+)").matcher(path);
        if (matcher.find())
        {
            return parseVersion(this, matcher.group("version"));
        }
        return fail("Unable to extract version from: $", path);
    }

    /**
     * Returns the root path under which the API is mounted. By default, this is <i>/[microservice-name]</i> (slash),
     * which means the API root path for version 1.0 would be <i>/[microservice-name]/api/1.0</i>. This method can be
     * overridden to put the API root under a microservice-specific path. For example, returning
     * <i>/</i> as the root path would result in this base URL for version 1.0 of the API:
     * <i>/api/1.0</i>
     *
     * @return The root path under which the API is found
     */
    protected final String rootPath()
    {
        return require(Microservice.class).rootPath();
    }

    /**
     * Returns the absolute API path. By default, this is <i>/api/[major-version].[minor-version]</i>
     *
     * @param version The API version
     * @return The path to the APi for the given version
     */
    protected String versionToPath(Version version)
    {
        return format("/api/$.$", version.major(), version.minor());
    }

    /**
     * The command line for mounted APIs should be a list of arguments, separated by commas
     *
     * @param commandLine The command line
     * @return The list of arguments
     */
    private StringList parseCommandLine(String commandLine)
    {
        return split(commandLine, ",");
    }
}

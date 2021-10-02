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

package com.telenav.kivakit.microservice.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.JettyMicroserviceResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.JettyMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.JettyOpenApiRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import io.swagger.v3.oas.models.info.Info;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Base class for KivaKit microservice REST applications. {@link Microservlet} request handlers ({@link
 * MicroservletGetRequest}, {@link MicroservletPostRequest} or {@link MicroservletDeleteRequest}) can be installed with
 * {@link #mount(String, Class)} or {@link #mount(String, Microservlet)} in the {@link #onInitialize()} method. The
 * {@link #gsonFactory()} method can be overridden to provide an application-specific {@link Gson} serializer. The
 * {@link #openApiInfo()} class can optionally be overridden to provide OpenAPI details beyond those provided by the
 * {@link Microservice} via {@link MicroserviceMetadata}.
 *
 * <p><b>Internal Details - Flow of Control</b></p>
 *
 * <ol>
 *     <li>Initializing</li>
 *     <ol>
 *         <li>{@link MicroserviceRestApplication} creates a {@link MicroservletJettyFilterPlugin}</li>
 *         <li>In the {@link MicroserviceRestApplication#onInitialize()} method, <i>mount*()</i> methods are used to bind
 *         {@link MicroservletRequest} handlers to paths</li>
 *     </ol>
 *     <li>Receiving requests</li>
 *     <ol>
 *         <li>An HTTP request is made to the {@link JettyMicroservletFilter} installed by {@link MicroservletJettyFilterPlugin}</li>
 *         <li>The {@link JettyMicroservletFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} resolves any
 *         {@link Microservlet} mounted on the request path. If no microservlet is found, the request is passed to the next
 *         {@link Filter} in the filter chain</li>
 *         <li>Request parameters are processed</li>
 *         <ol>
 *             <li>If the HTTP request method is GET, any path or query parameters are turned into a JSON object, which is processed as if it were posted</li>
 *             <li>If the HTTP request method is POST, then the posted JSON object is read by {@link JettyMicroservletRequest#readObject(Class)}</li>
 *             <li>If the HTTP request method is DELETE, any path or query parameters are turned into a JSON object, which is processed as if it were posted</li>
 *         </ol>
 *     </ol>
 *
 *     <li>Handling requests</li>
 *     <ol>
 *         <li>If the HTTP request method is GET, the {@link Microservlet#onGet(MicroservletRequest)} method is called</li>
 *         <li>If the HTTP request method is POST, the {@link Microservlet#onPost(MicroservletRequest)} method is called</li>
 *         <li>If the HTTP request method is DELETE, the {@link Microservlet#onDelete(MicroservletRequest)} method is called</li>
 *     </ol>
 *     <li>Producing a response</li>
 *     <ol>
 *         <li>The request handler's return value is passed to {@link JettyMicroserviceResponse#writeObject(MicroservletResponse)}, which:</li>
 *         <ol>
 *             <li>Validates the response object by calling {@link Validatable#validator()} and {@link Validator#validate(Listener)}</li>
 *             <li>Converts the object to JSON using the {@link Gson} object provided by {@link MicroserviceRestApplication#gsonFactory()}</li>
 *             <li>Writes the JSON object to the servlet response output stream</li>
 *         </ol>
 *     </ol>
 * </ol>
 *
 * @author jonathanl (shibo)
 * @see MicroserviceGsonFactory
 * @see MicroservletPostRequest
 * @see MicroservletGetRequest
 * @see MicroservletDeleteRequest
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public abstract class MicroserviceRestApplication extends BaseComponent implements Initializable
{
    /** The microservice that owns this REST application */
    @UmlAggregation
    private final Microservice microservice;

    /** True while the constructor is running */
    private boolean mountAllowed = false;

    /**
     * @param microservice The microservice that is creating this REST application
     */
    public MicroserviceRestApplication(final Microservice microservice)
    {
        this.microservice = microservice;
        microservice.listenTo(this);

        this.register(this);
    }

    /**
     * @return Factory that can create a {@link Gson} instance for serializing JSON object
     */
    @UmlRelation(label = "creates")
    public MicroserviceGsonFactory gsonFactory()
    {
        return new MicroserviceGsonFactory();
    }

    /**
     * Mount OpenAPI request handler and initialize the rest application. This method cannot be overridden. Override
     * {@link #onInitialize()} instead.
     */
    @Override
    public final void initialize()
    {
        mountAllowed = true;
        try
        {
            mount("/open-api/swagger.json", JettyOpenApiRequest.class);

            onInitialize();
        }
        finally
        {
            mountAllowed = false;
        }
    }

    /**
     * @return The microservice to which this rest application belongs
     */
    public Microservice microservice()
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
    public void mount(final String path, final Microservlet<?, ?> microservlet)
    {
        if (mountAllowed)
        {
            require(MicroservletJettyFilterPlugin.class).mount(path, microservlet);
        }
        else
        {
            problem("Request handlers must be mounted in onInitialize()");
        }
    }

    /**
     * Mounts the given request class on the given path.
     *
     * @param path The path to the given microservlet request handler (requestType). If the path is not absolute
     * (doesn't start with a '/'), it is prefixed with: "/api/[major.version].[minor.version]/", where the version is
     * retrieved from {@link Microservice#version()}. For example, the path "users" in microservlet version 3.1 will
     * resolve to "/api/3.1/users", and the path "/users" will resolve to "/users".
     * @param requestType The type of the request
     */
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mount(final String path, final Class<Request> requestType)
    {
        if (mountAllowed)
        {
            // Create a request object, so we can get the response type and HTTP method,
            final var request = Classes.newInstance(this, requestType);
            if (request != null)
            {
                listenTo(request);

                // then mount an anonymous microservlet on the given path,
                final Class<Response> responseType = (Class<Response>) request.responseType();
                ensureNotNull(responseType, "Request type ${class} has no response type", requestType);
                mount(path, listenTo(new Microservlet<Request, Response>(requestType, responseType)
                {
                    @Override
                    @JsonProperty
                    public String description()
                    {
                        return Message.format("Anonymous microservlet for ${class}", requestType());
                    }

                    @Override
                    public Response onDelete(final Request request)
                    {
                        // and if the microservlet receives a DELETE request, return the value of the request object.
                        return (Response) ((MicroservletDeleteRequest) request).onDelete();
                    }

                    @Override
                    public Response onGet(final Request request)
                    {
                        // and if the microservlet receives a GET request, return the value of the request object,
                        return (Response) ((MicroservletGetRequest) request).onGet();
                    }

                    @Override
                    public Response onPost(final Request request)
                    {
                        // and if the microservlet receives a POST request, return the value of the request object.
                        return (Response) ((MicroservletPostRequest) request).onPost();
                    }
                }).supportedMethods(ObjectSet.of(request.httpMethod())));
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
     * OpenAPI {@link Info} for the microservice. This method can be overridden to provide more detail that what is in
     * {@link MicroserviceMetadata}.
     */
    public Info openApiInfo()
    {
        // Get the microservice metadata,
        final var metadata = require(Microservice.class).metadata();

        // and add it to the OpenAPI object.
        return new Info()
                .version(metadata.version().toString())
                .description(metadata.description())
                .title(metadata.name());
    }
}

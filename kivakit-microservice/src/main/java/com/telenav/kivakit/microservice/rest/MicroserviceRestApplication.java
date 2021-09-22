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

import com.google.gson.Gson;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroserviceResponse;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.JettyOpenApiRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;
import com.telenav.kivakit.web.jersey.BaseRestApplication;
import com.telenav.kivakit.web.jersey.JerseyGsonSerializer;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import io.swagger.v3.oas.models.info.Info;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Base class for KivaKit microservice REST applications.
 *
 * <p><b>Flow of Control</b></p>
 *
 * <ol>
 *     <li>{@link MicroserviceRestApplication} creates a {@link MicroservletJettyFilterPlugin}</li>
 *     <li>In the {@link MicroserviceRestApplication} subclass constructor, to <i>mount*()</i> methods are used to bind {@link Microservlet}s to paths</li>
 *     <li>An HTTP request is made to the {@link JettyMicroservletFilter} installed by {@link MicroservletJettyFilterPlugin}</li>
 *     <li>The {@link JettyMicroservletFilter#doFilter(ServletRequest, ServletResponse, FilterChain)} method parses the request path and parameters</li>
 *     <li>The request path is used to resolve any {@link Microservlet} mounted on the path</li>
 *     <li>If the HTTP request method is POST, then the posted JSON object is read by {@link JettyMicroservletRequest#readObject(Class)}</li>
 *     <li>If the HTTP request method is POST, the {@link Microservlet#onPost(MicroservletRequest)} method is called</li>
 *     <li>If the HTTP request method is GET, the {@link Microservlet#onGet(MicroservletRequest)} method is called</li>
 *     <li>The return value from onPost() or onGet() is passed to {@link JettyMicroserviceResponse#writeObject(Object)}, which:</li>
 *     <ol>
 *         <li>Validates the response object by calling {@link Validatable#validator()} and {@link Validator#validate(Listener)}</li>
 *         <li>Converts the object to JSON using the {@link Gson} object provided by {@link MicroserviceRestApplication#gsonFactory()}</li>
 *         <li>Writes the JSON object to the servlet response output stream</li>
 *     </ol>
 * </ol>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public abstract class MicroserviceRestApplication extends BaseRestApplication
{
    /** The microservice that owns this REST application */
    @UmlAggregation
    private final Microservice microservice;

    /** The Jetty microservlet filter plugin for this REST application */
    @UmlAggregation
    private final MicroservletJettyFilterPlugin jettyMicroservlet;

    /**
     * @param microservice The microservice that is creating this REST application
     */
    public MicroserviceRestApplication(final Microservice microservice)
    {
        this.microservice = microservice;
        jettyMicroservlet = new MicroservletJettyFilterPlugin(this);

        register(new JerseyGsonSerializer<>(gsonFactory()));

        mount("/open-api", JettyOpenApiRequest.class);
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
     * @return The microservice to which this rest application belongs
     */
    public Microservice microservice()
    {
        return microservice;
    }

    /**
     * Mounts the given request class on the given path
     *
     * @param path The path to mount on
     * @param requestType The type of the request
     */
    @SuppressWarnings("unchecked")
    public <Request extends MicroservletRequest, Response extends MicroservletResponse>
    void mount(final String path, final Class<Request> requestType)
    {
        // Create a request object, so we can get the response type,
        final var request = Classes.newInstance(this, requestType);
        if (request != null)
        {
            // then mount an anonymous microservlet on the given path,
            final Class<Response> responseType = (Class<Response>) request.responseType();
            mount(path, listenTo(new Microservlet<Request, Response>(requestType, responseType)
            {
                @Override
                @SuppressWarnings("unchecked")
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
                @SuppressWarnings("unchecked")
                public Response onPost(final Request request)
                {
                    // and if the microservlet receives a POST request, return the value of the request object.
                    return (Response) ((MicroservletPostRequest) request).onPost();
                }
            }).supportedMethods(ObjectSet.of(request.httpMethod())));
        }
    }

    /**
     * Mounts the given microservlet on the given path
     *
     * @param path The path to the microservlet
     * @param microservlet The microservlet to mounds
     */
    @UmlRelation(label = "mounts", referent = Microservlet.class)
    public void mount(final String path, final Microservlet<?, ?> microservlet)
    {
        jettyMicroservlet.mount(path, microservlet);
    }

    /**
     * OpenAPI {@link Info} for this REST application. This method can be overridden to provide more detail that what is
     * in {@link MicroserviceMetadata}.
     */
    public Info openApiInfo()
    {
        // Get the microservice metadata,
        final var metadata = microservice.metadata();

        // and add it to the OpenAPI object.
        return new Info()
                .version(metadata.version().toString())
                .description(metadata.description())
                .title(metadata.name());
    }
}

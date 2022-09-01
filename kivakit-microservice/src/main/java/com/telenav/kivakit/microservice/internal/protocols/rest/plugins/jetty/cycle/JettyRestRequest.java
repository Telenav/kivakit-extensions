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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequest;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.messaging.Listener.emptyListener;

/**
 * <b>Not public API</b>
 * <p>
 * Represents an HTTP REST request to a microservlet in Jetty.
 *
 * <p>
 * The {@link #readRequest(Class)} method parses the JSON payload of a POST request into an object of the given type. It
 * then calls the {@link Validator} of the object. Parameters to the request (both path and query parameters) can be
 * retrieved with {@link #parameters()}. The requested path is available through {@link #path()}, and the version of the
 * REST application is provided by {@link #version()}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see BaseComponent
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyRestRequest extends BaseComponent implements
        RestRequest,
        RestProblemReportingTrait
{
    /** The request cycle to which this request belongs */
    @UmlAggregation
    private final JettyRestRequestCycle cycle;

    /** Servlet request */
    private final HttpServletRequest httpRequest;

    /** The properties for the request, from the path and/or query parameters */
    private PropertyMap properties;

    /**
     * @param cycle The request cycle for the {@link Microservlet}
     * @param httpRequest The Java Servlet API HTTP request object
     */
    public JettyRestRequest(JettyRestRequestCycle cycle, HttpServletRequest httpRequest)
    {
        this.cycle = cycle;
        this.httpRequest = httpRequest;
    }

    /**
     * Deserializes the given JSON to the given type using Gson
     *
     * @param json The JSON to deserialize
     * @param type The resulting object type
     * @return The deserialized object
     */
    @Override
    public <T> T fromJson(final String json, final Class<T> type)
    {
        return cycle.gson().fromJson(json, type);
    }

    /**
     * @return True if this request has a body that can be read with {@link #readRequest(Class)}
     */
    @Override
    public boolean hasBody()
    {
        return httpRequest.getContentLength() != 0;
    }

    /**
     * Returns the underlying {@link HttpServletRequest}
     */
    @Override
    public HttpServletRequest httpServletRequest()
    {
        return httpRequest;
    }

    /**
     * Opens servlet input stream for this request
     *
     * @return The input stream
     */
    @Override
    public ServletInputStream open()
    {
        return tryCatch(httpRequest::getInputStream, "Unable to open input stream");
    }

    /**
     * @return Parameters to this request
     */
    @Override
    public PropertyMap parameters()
    {
        return parameters(FilePath.parseFilePath(emptyListener(), ""));
    }

    /**
     * @return Parameters to this request
     */
    @Override
    public PropertyMap parameters(FilePath path)
    {
        if (properties == null)
        {
            properties = PropertyMap.create();

            try
            {
                // Parse the path in pairs, adding each to the properties map,
                if (path.size() % 2 != 0)
                {
                    problem(HttpStatus.BAD_REQUEST, "Path parameters must be paired");
                }
                else
                {
                    for (int i = 0; i < path.size(); i += 2)
                    {
                        properties.put(path.get(i), path.get(i + 1));
                    }

                    // then add any query parameters to the map.
                    var uri = URI.create(httpRequest.getRequestURI());
                    properties.addAll(QueryParameters.parse(this, uri.getQuery()).asMap());
                }
            }
            catch (Exception e)
            {
                problem(HttpStatus.BAD_REQUEST, e, "Invalid parameters: $", httpRequest.getRequestURI());
            }
        }
        return properties;
    }

    /**
     * @return The "context" path of the servlet from the root of the REST application
     */
    @Override
    @NotNull
    public FilePath path()
    {
        // Get the full request URI,
        var uri = httpRequest.getRequestURI();

        // and the context path,
        String contextPath = httpRequest.getContextPath();
        ensure(uri.startsWith(contextPath));

        // then return the URI without the context path
        return FilePath.parseFilePath(this, uri.substring(contextPath.length()));
    }

    /**
     * Retrieves an object from the JSON in the servlet request input stream.
     *
     * @param <T> The object type
     * @param requestType The type of object to deserialize from JSON
     * @return The deserialized object, or null if deserialization failed
     */
    @Override
    public <T extends MicroservletRequest> T readRequest(Class<T> requestType)
    {
        var response = cycle.restResponse();

        try
        {
            // Read JSON object from servlet input
            var in = open();
            String json = IO.string(in);
            var request = fromJson(json, requestType);

            // If the request is invalid (any problems go into the response object),
            if (!request.isValid(response))
            {
                // then we have an invalid response
                problem(HttpStatus.BAD_REQUEST, "Invalid request");
                return null;
            }

            return request;
        }
        catch (Exception e)
        {
            problem(HttpStatus.BAD_REQUEST, e, "Malformed request");
            return null;
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The version of the microservice for this request
     */
    @Override
    @KivaKitIncludeProperty
    public Version version()
    {
        return cycle.version();
    }
}

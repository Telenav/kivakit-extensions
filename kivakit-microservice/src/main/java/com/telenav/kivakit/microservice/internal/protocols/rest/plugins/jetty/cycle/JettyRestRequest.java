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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.RestProblemReportingTrait;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequest;
import com.telenav.kivakit.microservice.protocols.rest.http.Restful;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.validation.Validatable;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.io.IO.readString;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.network.http.HttpStatus.BAD_REQUEST;

/**
 * <b>Not public API</b>
 * <p>
 * Represents an HTTP REST request to a microservlet in Jetty.
 *
 * <p>
 * The {@link #readRequest(Class)} method parses the payload of a POST request into an object of the given type. It then
 * calls the {@link Validator} of the object. Parameters to the request (both path and query parameters) can be
 * retrieved with {@link #parameters()}. The requested path is available through {@link #path()}, and the version of the
 * REST application is provided by {@link Restful#apiVersion()}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see BaseComponent
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramJetty.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_SERVICE_PROVIDER)
public class JettyRestRequest extends BaseComponent implements
    TryTrait,
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
     * Returns true if this request has a body that can be read with {@link #readRequest(Class)}
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
     * Returns parameters to this request
     */
    @Override
    public PropertyMap parameters()
    {
        return parameters(parseFilePath(nullListener(), ""));
    }

    /**
     * Returns parameters to this request
     */
    @Override
    public PropertyMap parameters(FilePath path)
    {
        if (properties == null)
        {
            properties = PropertyMap.propertyMap();

            try
            {
                // Parse the path in pairs, adding each to the properties map,
                if (path.size() % 2 != 0)
                {
                    problem(BAD_REQUEST, "Path parameters must be paired");
                }
                else
                {
                    for (int i = 0; i < path.size(); i += 2)
                    {
                        properties.put(path.get(i), path.get(i + 1));
                    }

                    // then add any query parameters to the map.
                    var uri = URI.create(httpRequest.getRequestURI());
                    properties.addAll(QueryParameters.parseQueryParameters(this, uri.getQuery()).asVariableMap());
                }
            }
            catch (Exception e)
            {
                problem(BAD_REQUEST, e, "Invalid parameters: $", httpRequest.getRequestURI());
            }
        }
        return properties;
    }

    /**
     * Returns the "context" path of the servlet from the root of the REST application
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
        return filePath(uri.substring(contextPath.length()));
    }

    /**
     * Retrieves an object from the servlet request input stream.
     *
     * @param <T> The object type
     * @param requestType The type of object to deserialize
     * @return The deserialized object, or null if deserialization failed
     */
    @Override
    public <T extends MicroservletRequest> T readRequest(Class<T> requestType)
    {
        var response = cycle.restResponse();

        var in = open();
        var body = readString(this, in);

        try
        {
            // Read object from servlet input
            var request = restSerializer().deserialize(body, requestType);

            // If the request is invalid (any problems go into the response object),
            if (!request.isValid(response))
            {
                // then we have an invalid response
                problem(BAD_REQUEST, "Invalid request");
                return null;
            }

            return request;
        }
        catch (Exception e)
        {
            problem(BAD_REQUEST, e, "Malformed request: $", body);
            return null;
        }
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}

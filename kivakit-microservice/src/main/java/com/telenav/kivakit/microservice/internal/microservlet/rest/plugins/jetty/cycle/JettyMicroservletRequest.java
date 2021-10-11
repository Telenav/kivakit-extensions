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

package com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.cycle;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.internal.microservlet.rest.cycle.ProblemReportingTrait;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * <b>Not public API</b>
 * <p>
 * Represents a request to a microservlet.
 *
 * <p>
 * The {@link #readObject(Class)} method parses the JSON payload of a POST request into an object of the given type. It
 * then calls the {@link Validator} of the object. Parameters to the request (both path and query parameters) can be
 * retrieved with {@link #parameters()}. The requested path is available through {@link #path()}, and the version of the
 * REST application is provided by {@link #version()}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see BaseComponent
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletRequest extends BaseComponent implements ProblemReportingTrait
{
    /** The request cycle to which this request belongs */
    @UmlAggregation
    private final JettyMicroservletRequestCycle cycle;

    /** Servlet request */
    private final HttpServletRequest httpRequest;

    /** The properties for the request, from the path and/or query parameters */
    private PropertyMap properties;

    /**
     * @param cycle The request cycle for the {@link Microservlet}
     * @param httpRequest The Java Servlet API HTTP request object
     */
    public JettyMicroservletRequest(final JettyMicroservletRequestCycle cycle, final HttpServletRequest httpRequest)
    {
        this.cycle = cycle;
        this.httpRequest = httpRequest;
    }

    /**
     * @return Parameters to this request
     */
    public PropertyMap parameters()
    {
        return parameters(null);
    }

    /**
     * @return Parameters to this request
     */
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
                    problem(SC_BAD_REQUEST, "Path parameters must be paired");
                }
                else
                {
                    for (int i = 0; i < path.size(); i += 2)
                    {
                        properties.put(path.get(i), path.get(i + 1));
                    }

                    // then add any query parameters to the map.
                    final var uri = URI.create(httpRequest.getRequestURI());
                    properties.addAll(QueryParameters.parse(uri.getQuery()).asMap());
                }
            }
            catch (Exception e)
            {
                problem(SC_BAD_REQUEST, e, "Invalid parameters: $", httpRequest.getRequestURI());
            }
        }
        return properties;
    }

    /**
     * @return The "context" path of the servlet from the root of the REST application
     */
    @NotNull
    public FilePath path()
    {
        // Get the full request URI,
        final var uri = httpRequest.getRequestURI();

        // and the context path,
        final String contextPath = httpRequest.getContextPath();
        ensure(uri.startsWith(contextPath));

        // then return the URI without the context path
        return FilePath.parseFilePath(uri.substring(contextPath.length()));
    }

    /**
     * Retrieves an object from the JSON in the servlet request input stream.
     *
     * @param <T> The object type
     * @param requestType The type of object to deserialize from JSON
     * @return The deserialized object, or null if deserialization failed
     */
    public <T extends MicroservletRequest> T readObject(final Class<T> requestType)
    {
        final var response = cycle.response();

        try
        {
            // Read JSON object from servlet input
            final var in = this.httpRequest.getInputStream();
            final String json = IO.string(in);
            final var request = cycle.gson().fromJson(json, requestType);

            // If the request is invalid (any problems go into the response object),
            if (!request.isValid(response))
            {
                // then we have an invalid response
                response.problem(SC_BAD_REQUEST, "Invalid request object: $", json);
                return null;
            }

            return request;
        }
        catch (final Exception e)
        {
            problem(SC_BAD_REQUEST, e, "Unable to read JSON request from servlet input stream: $", e.getMessage());
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
    @KivaKitIncludeProperty
    public Version version()
    {
        return cycle.application()
                .microservice()
                .version();
    }
}

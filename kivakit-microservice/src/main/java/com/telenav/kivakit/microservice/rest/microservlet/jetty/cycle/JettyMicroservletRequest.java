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

package com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.network.core.QueryParameters;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
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
public class JettyMicroservletRequest extends BaseComponent
{
    /** The request cycle to which this request belongs */
    private final JettyMicroservletRequestCycle cycle;

    /** Servlet request */
    private final HttpServletRequest request;

    /** The properties for the request, from the path and/or query parameters */
    private PropertyMap properties;

    /**
     * @param cycle The request cycle for the {@link Microservlet}
     * @param request The Java Servlet API HTTP request object
     */
    public JettyMicroservletRequest(JettyMicroservletRequestCycle cycle, HttpServletRequest request)
    {
        this.cycle = cycle;
        this.request = request;
    }

    /**
     * @return Parameters to this request
     */
    public PropertyMap parameters()
    {
        if (properties == null)
        {
            // Get the full request URI,
            var uri = URI.create(request.getRequestURI());

            // parse the path in pairs, adding each to the properties map,
            var path = FilePath.filePath(uri);
            for (int i = 0; i < path.size(); i += 2)
            {
                properties.put(path.get(i), path.get(i + 1));
            }

            // then add any query parameters to the map.
            properties.addAll(QueryParameters.parse(uri.getQuery()).asMap());
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
        var uri = request.getRequestURI();

        // and the context path,
        String contextPath = request.getContextPath();
        ensure(uri.startsWith(contextPath));

        // then return the URI without the context path
        return FilePath.parseFilePath(uri.substring(contextPath.length()));
    }

    /**
     * Retrieves an object from the JSON in the servlet request input stream.
     *
     * @param requestType The type of object to deserialize from JSON
     * @param <T> The object type
     * @return The deserialized object, or null if deserialization failed
     */
    public <T> T readObject(Class<T> requestType)
    {
        try
        {
            // Read JSON object from servlet input
            var in = request.getInputStream();
            var object = cycle.gson().fromJson(IO.string(in), requestType);

            // and validate it if we can
            if (object instanceof Validatable)
            {
                ((Validatable) object).validator().validate(this);
            }
            
            return object;
        }
        catch (Exception e)
        {
            problem(e, "Unable to read JSON request from servlet input stream");
            return null;
        }
    }

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

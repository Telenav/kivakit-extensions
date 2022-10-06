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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequest;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.properties.PropertyMap;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Interface for abstracting HTTP REST requests
 *
 * @author jonathanl (shibo)
 * @see JettyRestRequest
 * @see JettyRestRequestCycle
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_PRIVATE)
public interface RestRequest extends Restful
{
    /**
     * @return True if this request has a body that can be read with {@link #readRequest(Class)}
     */
    boolean hasBody();

    /**
     * Returns the underlying {@link HttpServletRequest}
     */
    HttpServletRequest httpServletRequest();

    /**
     * Opens servlet input stream for this request
     *
     * @return The input stream
     */
    ServletInputStream open();

    /**
     * @return Parameters to this request
     */
    PropertyMap parameters();

    /**
     * @return Parameters to this request
     */
    PropertyMap parameters(FilePath path);

    /**
     * @return The "context" path of the servlet from the root of the REST application
     */
    @NotNull
    FilePath path();

    /**
     * Reads a {@link MicroservletRequest} object from the JSON in the servlet request input stream.
     *
     * @param <T> The object type
     * @param requestType The type of object to deserialize from JSON
     * @return The deserialized object, or null if deserialization failed
     */
    <T extends MicroservletRequest> T readRequest(Class<T> requestType);
}

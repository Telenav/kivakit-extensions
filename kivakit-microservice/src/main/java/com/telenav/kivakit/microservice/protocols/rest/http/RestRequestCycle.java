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

import com.google.gson.Gson;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.microservlet.Microservlet;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A Jetty HTTP request/response cycle.
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #microservlet()}</li>
 *     <li>{@link #restRequest()}</li>
 *     <li>{@link #restResponse()}</li>
 *     <li>{@link #restService()}</li>
 * </ul>
 *
 * <p><b>Gson Serialization</b></p>
 *
 * <ul>
 *     <li>{@link #gson()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see JettyRestRequestCycle
 * @see Microservlet
 * @see RestService
 * @see RestRequest
 * @see RestResponse
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public interface RestRequestCycle
{
    /**
     * Returns a Gson instance provided by the REST application
     */
    default Gson gson()
    {
        var pretty = restRequest().parameters().asBoolean("pretty", false);
        return restService()
                .microservice()
                .gsonFactory()
                .prettyPrinting(pretty)
                .gson();
    }

    /**
     * Returns the {@link Microservlet} handling this request cycle
     */
    Microservlet<?, ?> microservlet();

    /**
     * Returns the REST request
     */
    RestRequest restRequest();

    /**
     * Returns the REST response
     */
    RestResponse restResponse();

    /**
     * Returns the REST application that owns this request cycle
     */
    RestService restService();
}

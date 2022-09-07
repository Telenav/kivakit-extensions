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
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.microservlet.Microservlet;

/**
 * A Jetty HTTP request/response cycle.
 *
 * @author jonathanl (shibo)
 * @see JettyRestRequestCycle
 * @see Microservlet
 * @see RestService
 * @see RestRequest
 * @see RestResponse
 */
public interface RestRequestCycle
{
    /**
     * @return A Gson instance provided by the REST application
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
     * @return The {@link Microservlet} handling this request cycle
     */
    Microservlet<?, ?> microservlet();

    /**
     * @return The REST request
     */
    RestRequest restRequest();

    /**
     * @return The REST response
     */
    RestResponse restResponse();

    /**
     * @return The REST application that owns this request cycle
     */
    RestService restService();
}
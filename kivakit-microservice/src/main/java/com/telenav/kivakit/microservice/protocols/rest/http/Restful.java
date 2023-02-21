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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.microservice.protocols.rest.http.RestRequestThread.requestCycle;

/**
 * Base interface for {@link RestRequest} and {@link RestResponse}, as well as {@link MicroservletRequest} and
 * {@link MicroservletResponse}. Provides easy access to {@link RestRequestCycle}, {@link RestRequest},
 * {@link RestResponse}, {@link RestService}, and to JSON serialization.
 *
 * <p><b>Ownership</b></p>
 * <ul>
 *     <li>{@link #restService()} - The rest service that owns this</li>
 *     <li>{@link #microservice()} - The microservice that owns this</li>
 *     <li>{@link #apiVersion()} - The version of this REST API</li>
 * </ul>
 *
 * <p><b>Request Cycle</b></p>
 * <ul>
 *     <li>{@link #restRequestCycle()} - The current request cycle</li>
 *     <li>{@link #restRequest()} - The current request</li>
 *     <li>{@link #restResponse()} - The current response</li>
 * </ul>
 *
 * <p><b>Serialization</b></p>
 * <ul>
 *     <li>{@link #defaultRestSerializer()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see RestRequest
 * @see RestResponse
 * @see RestProblemReportingTrait
 * @see MicroservletRequest
 * @see MicroservletResponse
 */
@SuppressWarnings({ "unused" })
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface Restful extends Component
{
    /**
     * Returns the version of the microservice that is responding to a request
     */
    @FormatProperty
    default Version apiVersion()
    {
        return restService().apiVersion();
    }

    /**
     * Returns a rest serializer for serializing requests and responses
     *
     * @return The serializer
     */
    default <Request extends MicroservletRequest, Response extends MicroservletResponse> RestSerializer<Request, Response> defaultRestSerializer()
    {
        return restService().restSerializer();
    }

    /**
     * Returns the microservice that is responding to a REST request
     */
    @FormatProperty
    default Microservice<?> microservice()
    {
        return restService().microservice();
    }

    /**
     * Returns the REST request for the current request cycle
     */
    default RestRequest restRequest()
    {
        return restRequestCycle().restRequest();
    }

    /**
     * Returns the HTTP REST request cycle associated with the calling thread
     */
    default RestRequestCycle restRequestCycle()
    {
        return requestCycle();
    }

    /**
     * Returns the REST response for the current request cycle
     */
    default RestResponse restResponse()
    {
        return restRequestCycle().restResponse();
    }

    /**
     * Returns the REST service handling requests
     */
    default RestService restService()
    {
        return restRequestCycle().restService();
    }
}

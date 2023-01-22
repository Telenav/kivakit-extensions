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
import com.google.gson.JsonElement;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.Component;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.serialization.gson.GsonFactorySource;

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
 *     <li>{@link #version()} - The version of {@link #microservice()}</li>
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
 *     <li>{@link #fromJson(String, Class)} - Converts JSON to an object</li>
 *     <li>{@link #toJson(Object)} - Converts an object to JSON</li>
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
     * Deserializes the given JSON to the given type using Gson
     *
     * @param json The JSON to deserialize
     * @param type The resulting object type
     * @return The deserialized object
     */
    default <T> T fromJson(String json, Class<T> type)
    {
        return restRequestCycle().gson().fromJson(json, type);
    }

    /**
     * Provides a {@link Gson} serializer for the given object. If the object implements {@link GsonFactorySource}, that
     * interface provides the serializer, otherwise, the {@link Gson} instance is provided by the request cycle
     *
     * @param object The object
     * @return The {@link Gson} serializer for the object
     */
    default Gson gson(Object object)
    {
        // If the response object has a custom GsonFactory,
        if (object instanceof GsonFactorySource)
        {
            // use that to convert the response to JSON,
            return ((GsonFactorySource) object).gsonFactory().gson();
        }
        else
        {
            // otherwise, use the GsonFactory, provided by the application through the request cycle.
            return restRequestCycle().gson();
        }
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

    /**
     * @param response The response object to be serialized
     * @return The object serialized into JSON format, using the application {@link GsonFactory}. This behavior can be
     * overridden by implementing {@link GsonFactorySource} to provide a custom {@link GsonFactory} for a given response
     * object.
     */
    default JsonElement toJson(Object response)
    {
        // We will serialize the response object itself by default.
        var objectToSerialize = response;

        // If the response object provides another object to serialize,
        if (response instanceof MicroserviceGsonObjectSource)
        {
            // then make that the object to serialize.
            objectToSerialize = ((MicroserviceGsonObjectSource) response).gsonObject();
        }

        return gson(response).toJsonTree(objectToSerialize);
    }

    /**
     * Returns the version of the microservice that is responding to a request
     */
    @FormatProperty
    @OpenApiIncludeMember(title = "Version", description = "The microservice version")
    default Version version()
    {
        return microservice().version();
    }
}

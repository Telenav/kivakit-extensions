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

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * Defines the request and response of a REST method.
 * </p>
 * The subclass implements {@link #newResponse()} to create a {@link MicroserviceResponse} when {@link #respond()} is
 * called. It overrides {@link #onRespond(MicroserviceResponse)} to populate the response (if the request is valid).
 *
 * @author jonathanl (shibo)
 * @see Validatable
 * @see BaseComponent
 */
public abstract class MicroserviceRestRequest<Response extends MicroserviceRestRequest.MicroserviceResponse> extends BaseComponent implements Validatable
{
    /**
     * REST methods
     */
    public enum Method
    {
        POST,
        GET,
        PUT,
        DELETE
    }

    /**
     * Base REST response. Holds a (JSON serialized) problem, as well as whatever fields are in the subclass. The {@link
     * #onTransmitting(Transmittable)} method captures any problems that are broadcast to this response object.
     *
     * @author jonathanl (shibo)
     */
    public abstract static class MicroserviceResponse extends BaseComponent
    {
        @KivaKitExcludeProperty
        private Problem problem;

        /**
         * @return This response as a {@link Result} object
         */
        public Result<MicroserviceResponse> asResult()
        {
            return problem != null
                    ? Result.failed(problem)
                    : Result.succeeded(this);
        }

        /**
         * Capture any problems that this response is about to send
         */
        @Override
        public void onTransmitting(final Transmittable message)
        {
            if (message instanceof Problem)
            {
                problem = (Problem) message;
            }
        }

        @KivaKitIncludeProperty
        @Schema(description = "A description of any problem that might have occurred")
        public Problem problem()
        {
            return problem;
        }

        public String toString()
        {
            return new ObjectFormatter(this).toString();
        }

        @KivaKitIncludeProperty
        @Schema(description = "The server version")
        public Version version()
        {
            return Application.get().version();
        }
    }

    /**
     * Responds to this request.
     *
     * <ol>
     *     <li>Creates a {@link MicroserviceResponse} by calling {@link #newResponse()}</li>
     *     <li>Validates this request by calling {@link #isValid(Listener)} with the response object as the {@link Listener} to capture any {@link Problem}s</li>
     *     <ol type="a">
     *         <li>If the request is valid, calls {@link #onRespond(MicroserviceResponse)} to allow population of the response</li>
     *         <li>If the request is invalid, returns the captured problem</li>
     *     </ol>
     * </ol>
     *
     * @return The response
     */
    public final Response respond(Listener listener)
    {
        trace("Request: $", this);

        // Create the response object,
        var response = newResponse();
        listener.listenTo(response);

        // and if this request is valid,
        if (isValid(response))
        {
            // then let the subclass populate the response,
            onRespond(response);
            trace("Response: $", response);
        }
        else
        {
            // otherwise warn the caller.s
            response.warning("Request is invalid: $", this);
        }

        // Return the response. If the request was invalid, it will have captured any problems in
        // the call above to isValid(response).
        return response;
    }

    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @KivaKitIncludeProperty
    public Version version()
    {
        return Application.get().version();
    }

    /**
     * @return The REST method for this API method
     */
    protected abstract Method method();

    /**
     * @return A new response object, created by the subclass
     */
    protected abstract Response newResponse();

    /**
     * Populates the {@link Response} response object that was created by {@link #newResponse()}.
     *
     * @param response The response to populate
     */
    protected abstract void onRespond(final Response response);
}

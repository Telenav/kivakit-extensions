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
import com.telenav.kivakit.annotations.code.CodeType;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.network.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Interface for abstracting HTTP REST requests
 *
 * @author jonathanl (shibo)
 * @see JettyRestResponse
 * @see JettyRestRequestCycle
 */
@SuppressWarnings("unused")
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CodeType.CODE_INTERNAL)
public interface RestResponse extends RestProblemReportingTrait
{
    /**
     * Adds the given header value to the response
     *
     * @param header The header key
     * @param value The value
     */
    default void addHeader(String header, String value)
    {
        httpServletResponse().setHeader(header, value);
    }

    /**
     * The errors for this response
     */
    MicroservletErrorResponse errors();

    /**
     * @return The underlying {@link HttpServletResponse}
     */
    HttpServletResponse httpServletResponse();

    /**
     * @return The HTTP status for this response
     */
    HttpStatus httpStatus();

    /**
     * Sets the HTTP status for this response
     *
     * @param status The status
     */
    void httpStatus(HttpStatus status);

    /**
     * Reports the given problem and associated {@link HttpStatus}
     */
    @Override
    default Problem problem(HttpStatus httpStatus, String text, Object... arguments)
    {
        return problem(httpStatus, null, text, arguments);
    }

    /**
     * Reports the given problem and associated {@link HttpStatus}
     */
    @Override
    default Problem problem(HttpStatus httpStatus, Throwable exception, String text, Object... arguments)
    {
        return transmit(new HttpProblem(httpStatus, exception, text, arguments));
    }

    /**
     * Writes the given response object to the servlet output stream in JSON format. If the request is invalid, or the
     * response is null or invalid, a {@link MicroservletErrorResponse} is sent with the captured error messages.
     *
     * @param response The response to write to the HTTP output stream
     */
    void writeResponse(MicroservletResponse response);
}

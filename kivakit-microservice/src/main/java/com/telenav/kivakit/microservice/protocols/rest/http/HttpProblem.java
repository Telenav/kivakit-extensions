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
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.network.http.HttpStatus;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Represents a problem with an associated {@link HttpStatus} code.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class HttpProblem extends Problem
{
    private final HttpStatus httpStatus;

    public HttpProblem(HttpStatus httpStatus, String message, Object... arguments)
    {
        this(httpStatus, null, message, arguments);
    }

    public HttpProblem(HttpStatus httpStatus, Throwable cause, String message, Object... arguments)
    {
        super(cause, message, arguments);
        this.httpStatus = httpStatus;
    }

    /**
     * Returns the HTTP status associated with this problem
     */
    public HttpStatus httpStatus()
    {
        return httpStatus;
    }
}

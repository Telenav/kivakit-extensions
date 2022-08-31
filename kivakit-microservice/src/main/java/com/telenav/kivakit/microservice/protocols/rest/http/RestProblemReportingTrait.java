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

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.network.http.HttpStatus;

/**
 * Allows reporting of problems and status through the {@link RestResponse}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface RestProblemReportingTrait extends Restful
{
    default void okay(String text, Object... arguments)
    {
        restResponse().httpStatus(HttpStatus.OK);
        restResponse().information(text, arguments);
    }

    default Problem problem(HttpStatus httpStatus, String text, Object... arguments)
    {
        return restResponse().problem(httpStatus, text, arguments);
    }

    default Problem problem(HttpStatus httpStatus, Throwable exception, String text, Object... arguments)
    {
        return restResponse().problem(httpStatus, exception, text + (exception == null
                ? ""
                : ": " + exception.getMessage()), arguments);
    }
}

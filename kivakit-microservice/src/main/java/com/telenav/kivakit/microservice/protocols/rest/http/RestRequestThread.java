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

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Maintains a thread-local {@link RestRequestCycle}. When a request is handled, the request cycle is attached to the
 * curren thread with {@link #requestThreadAttach(RestRequestCycle)}. When the response finished, the request cycle is detached again
 * with {@link #requestThreadDetach()}. During request handling, the method {@link #requestCycle()} returns the request cycle for the
 * current thread.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class RestRequestThread
{
    /** A thread local variable holding the request cycle for a given thread using this servlet */
    private static final ThreadLocal<RestRequestCycle> threadToRequestCycle = new ThreadLocal<>();

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Associates the given request cycle with the calling thread
     * </p>
     */
    public static void requestThreadAttach(RestRequestCycle cycle)
    {
        threadToRequestCycle.set(cycle);
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Disassociates any request cycle from the calling thread
     * </p>
     */
    public static void requestThreadDetach()
    {
        requestThreadAttach(null);
    }

    /**
     * Returns the request cycle associated with the calling thread
     */
    public static RestRequestCycle requestCycle()
    {
        return threadToRequestCycle.get();
    }
}

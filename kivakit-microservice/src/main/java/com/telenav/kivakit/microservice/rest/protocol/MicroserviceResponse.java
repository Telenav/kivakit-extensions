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

package com.telenav.kivakit.microservice.rest.protocol;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Base REST response. Holds a (JSON serialized) problem, as well as whatever fields are in the subclass. The {@link
 * #onTransmitting(Transmittable)} method captures any problems that are broadcast to this response object.
 *
 * @author jonathanl (shibo)
 */
public abstract class MicroserviceResponse extends BaseComponent
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

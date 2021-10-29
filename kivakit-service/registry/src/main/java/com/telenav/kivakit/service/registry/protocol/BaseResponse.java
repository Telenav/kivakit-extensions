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

package com.telenav.kivakit.service.registry.protocol;

import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.service.registry.ServiceRegistrySettings;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base REST response for service registries
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramRest.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseResponse<T>
{
    private Problem problem;

    public Result<T> asResult()
    {
        return problem != null ? Result.failed(problem) : Result.succeeded(value());
    }

    public BaseResponse<T> problem(String message, Object... arguments)
    {
        problem = new Problem(message, arguments);
        return this;
    }

    @KivaKitIncludeProperty
    public Problem problem()
    {
        return problem;
    }

    public void result(Result<T> result)
    {
        value(result.get());
        problem = (Problem) result.why();
    }

    @KivaKitIncludeProperty
    public Version version()
    {
        return Settings.of(this).requireSettings(ServiceRegistrySettings.class).version();
    }

    protected abstract void value(T value);

    protected abstract T value();
}

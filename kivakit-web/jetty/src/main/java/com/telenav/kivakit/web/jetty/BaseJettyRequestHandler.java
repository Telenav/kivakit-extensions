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

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.web.jetty.resources.BaseAssetsJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseFilterJettyPlugin;
import com.telenav.kivakit.web.jetty.resources.BaseServletJettyPlugin;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Base class for all types of Jetty request handlers. Has a {@link #name()} and {@link #path()}.
 *
 * @author jonathanl (shibo)
 * @see BaseFilterJettyPlugin
 * @see BaseAssetsJettyPlugin
 * @see BaseServletJettyPlugin
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public abstract class BaseJettyRequestHandler extends BaseComponent implements Named
{
    private final String name;

    private String path;

    protected BaseJettyRequestHandler(String name)
    {
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }

    protected void path(String path)
    {
        this.path = path;
    }

    protected String path()
    {
        return path;
    }
}

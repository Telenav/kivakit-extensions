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

package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.web.jetty.BaseJettyRequestHandler;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Base class for request handlers that serve up file resources.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class BaseAssetsJettyPlugin extends BaseJettyRequestHandler
{
    public BaseAssetsJettyPlugin(String name)
    {
        super(name);
    }

    /**
     * @return The Jetty-specific holder of a static resource servlet, normally {@link DefaultServlet}.
     */
    public abstract ServletHolder holder();
}

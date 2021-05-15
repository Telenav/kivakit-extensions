////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.web.jetty.resources.BaseJettyFilter;
import com.telenav.kivakit.web.jetty.resources.BaseJettyResource;
import com.telenav.kivakit.web.jetty.resources.BaseJettyServlet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Base class for all types of Jetty request handlers. Has a {@link #name()} and {@link #path()}.
 *
 * @author jonathanl (shibo)
 * @see BaseJettyFilter
 * @see BaseJettyResource
 * @see BaseJettyServlet
 */
@LexakaiJavadoc(complete = true)
public abstract class BaseJettyRequestHandler implements Named
{
    private String path;

    private final String name;

    public BaseJettyRequestHandler(final String name)
    {
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }

    protected void path(final String path)
    {
        this.path = path;
    }

    protected String path()
    {
        return path;
    }
}
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

package com.telenav.kivakit.web.swagger;

import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.web.jetty.resources.BaseJettyResourcePlugin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;

/**
 * Provides the Swagger JavaScript resources required to show Swagger documentation for the given REST {@link
 * Application}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class SwaggerWebJarJettyResourcePlugin extends BaseJettyResourcePlugin
{
    private final Application application;

    public SwaggerWebJarJettyResourcePlugin(final Application application)
    {
        super("[SwaggerWebJarJettyResourcePlugin application = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        final var holder = new ServletHolder(name(), new DefaultServlet());
        holder.setInitParameter("resourceBase", resourceBase());
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");
        return holder;
    }

    String resourceBase()
    {
        final var path = "META-INF/resources/webjars/swagger-ui/3.51.2";
        return Classes.resourceUri(application.getClass(), path).toString();
    }
}

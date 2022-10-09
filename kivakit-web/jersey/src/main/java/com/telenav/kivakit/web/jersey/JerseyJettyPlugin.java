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

package com.telenav.kivakit.web.jersey;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.BaseServletJettyPlugin;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * {@link BaseServletJettyPlugin} plugin that can be added to {@link JettyServer} to serve REST resources from the
 * {@link BaseRestApplication} application passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class JerseyJettyPlugin extends BaseServletJettyPlugin
{
    private final ResourceConfig application;

    /**
     * @param application The REST application
     */
    public JerseyJettyPlugin(ResourceConfig application)
    {
        super("[JerseyJettyPlugin application = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        // Get the fully qualified class name of the JAX-RS application,
        var name = application.getClass().getName();

        // create a "ServletContainer" for the application (this object may look like it's part of
        // the Servlet API, but it would be better named "RestServlet"),
        var restServlet = new ServletContainer(application);

        // create a Jetty-specific "ServletHolder" for the servlet
        var jersey = new ServletHolder(name, restServlet);

        // and initialize it with the name of the JAX-RS application.
        jersey.setInitOrder(0);
        jersey.setInitParameter("javax.ws.rs.Application", name);

        return jersey;
    }
}

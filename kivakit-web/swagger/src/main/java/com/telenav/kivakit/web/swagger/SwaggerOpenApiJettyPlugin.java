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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.web.jetty.resources.BaseServletJettyPlugin;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.OpenApiServlet;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import jakarta.ws.rs.core.Application;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Produces the swagger.json OpenAPI interface description for the given application.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class SwaggerOpenApiJettyPlugin extends BaseServletJettyPlugin
{
    /** The underlying REST application */
    private final Application application;

    /**
     * @param application The REST application
     */
    public SwaggerOpenApiJettyPlugin(Application application)
    {
        super("[SwaggerOpenApiJettyPlugin application = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServletHolder holder()
    {
        var configuration = new SwaggerConfiguration()
                .prettyPrint(true)
                .resourcePackages(Set.of(application.getClass().getPackageName()));

        try
        {
            OpenApiServlet servlet = new OpenApiServlet();

            new JaxrsOpenApiContextBuilder<>()
                    .servletConfig(servlet)
                    .application(application)
                    .openApiConfiguration(configuration)
                    .buildContext(true);

            return new ServletHolder(servlet);
        }
        catch (OpenApiConfigurationException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

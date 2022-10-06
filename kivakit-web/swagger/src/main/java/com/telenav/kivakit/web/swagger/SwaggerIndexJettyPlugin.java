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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.web.jetty.resources.BaseAssetsJettyPlugin;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Provides the Swagger <i>index.html</i> resource
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class SwaggerIndexJettyPlugin extends BaseAssetsJettyPlugin
{
    /**
     * Servlet that produces the Swagger <i>index.html</i> static resource
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
                 testing = TESTING_NONE,
                 documentation = DOCUMENTATION_COMPLETE)
    class IndexServlet extends HttpServlet
    {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
        {
            response.setContentType("text/html");
            try (var out = response.getWriter())
            {
                out.println(index());
            }
        }
    }

    /** The folder of OpenAPI resources to serve */
    private final ResourceFolder<?> folder;

    /** The port that swagger should be accessible on */
    private final int port;

    /**
     * @param folder The folder containing Swagger static resources
     * @param port The port where Swagger is running
     */
    public SwaggerIndexJettyPlugin(ResourceFolder<?> folder, int port)
    {
        super("[SwaggerIndexJettyPlugin folder = " + folder + "]");
        this.folder = folder;
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServletHolder holder()
    {
        return new ServletHolder(name(), new IndexServlet());
    }

    String index()
    {
        return folder.resource("index.html")
                .reader()
                .asString()
                .replaceAll("PORT", Integer.toString(port));
    }
}

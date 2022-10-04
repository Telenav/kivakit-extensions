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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.web.jetty.resources.BaseAssetsJettyPlugin;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Provides the Swagger <a href="https://www.webjars.org">WebJar</a> static resource required to show Swagger
 * documentation
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class SwaggerWebJarAssetJettyPlugin extends BaseAssetsJettyPlugin
{
    public SwaggerWebJarAssetJettyPlugin()
    {
        super("[SwaggerWebJarAssetJettyPlugin]");
    }

    @Override
    public ServletHolder holder()
    {
        var holder = new ServletHolder(name(), new DefaultServlet());
        holder.setInitParameter("resourceBase", resourceBase());
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");
        return holder;
    }

    private String resourceBase()
    {
        var path = "META-INF/resources/webjars/swagger-ui/3.51.2";
        return Classes.resourceUri(SwaggerIndexJettyPlugin.class, path).toString();
    }
}

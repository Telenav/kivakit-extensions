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

package com.telenav.kivakit.metrics.prometheus;

import com.telenav.kivakit.web.jetty.resources.BaseJettyServletPlugin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import io.prometheus.client.exporter.MetricsServlet;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Produces the swagger.json OpenAPI interface description for the given application.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class PrometheusJettyServletPlugin extends BaseJettyServletPlugin
{
    public PrometheusJettyServletPlugin()
    {
        super("[PrometheusJettyServletPlugin]");
    }

    @Override
    public ServletHolder holder()
    {
        return new ServletHolder(new MetricsServlet());
    }
}

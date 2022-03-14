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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.microservice.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.eclipse.jetty.servlet.FilterHolder;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Installs and configures the {@link JettyMicroservletFilter} required to handle REST requests.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservletFilterHolder extends FilterHolder implements ComponentMixin
{
    @UmlAggregation
    private final JettyMicroservletFilter filter;

    public JettyMicroservletFilterHolder(MicroserviceRestService application)
    {
        setFilter(filter = listenTo(new JettyMicroservletFilter(application)));
    }

    public JettyMicroservletFilter filter()
    {
        return filter;
    }
}

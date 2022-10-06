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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.eclipse.jetty.servlet.FilterHolder;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_SERVICE_PROVIDER_IMPLEMENTATION;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Installs and configures the {@link JettyMicroservletFilter} required to handle REST requests.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_SERVICE_PROVIDER_IMPLEMENTATION)
public class JettyMicroservletFilterHolder extends FilterHolder implements ComponentMixin
{
    @UmlAggregation
    private final JettyMicroservletFilter filter;

    public JettyMicroservletFilterHolder(RestService application)
    {
        setFilter(filter = listenTo(new JettyMicroservletFilter(application)));
    }

    public JettyMicroservletFilter filter()
    {
        return filter;
    }
}

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

package com.telenav.kivakit.microservice.rest.microservlet.jetty;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilterHolder;
import com.telenav.kivakit.web.jetty.resources.BaseJettyFilter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.apache.wicket.protocol.http.WebApplication;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;

/**
 * A Jetty filter for Apache Wicket applications. The Wicket {@link WebApplication} class is passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyMicroservlet extends BaseJettyFilter
{
    /** The Jetty holder for an instance of {@link JettyMicroservletFilter} */
    @UmlAggregation
    private JettyMicroservletFilterHolder filterHolder;

    /**
     * @param application The REST application
     */
    public JettyMicroservlet(final MicroserviceRestApplication application)
    {
        super(application.getClass().getSimpleName());

        application.listenTo(new JettyMicroservletFilterHolder(application));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnumSet<DispatcherType> dispatchers()
    {
        return EnumSet.of(ASYNC, ERROR, FORWARD, INCLUDE, REQUEST);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JettyMicroservletFilterHolder holder()
    {
        return filterHolder;
    }

    /**
     * Forwards requests to mount microservlets to the filter holder
     */
    public void mount(final String path, final Microservlet<?, ?> microservlet)
    {
        filterHolder.mount(path, microservlet);
    }
}

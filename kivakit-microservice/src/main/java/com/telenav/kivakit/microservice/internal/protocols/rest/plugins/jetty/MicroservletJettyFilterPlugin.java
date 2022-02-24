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

package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty;

import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilterHolder;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.BaseJettyFilterPlugin;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;

/**
 * <b>Not public API</b>
 *
 * <p>
 * A {@link JettyServer} filter plugin for {@link Microservlet}s.
 * </p>
 *
 * <p>
 * Creates a {@link JettyMicroservletFilterHolder} that creates a {@link JettyMicroservletFilter}.
 * </p>
 *
 * <p><b>NOTE</b></p>
 *
 * <p>
 * All classes in the <i>com.telenav.kivakit.microservice.rest.microservlet.jetty</i> package are implementation details
 * and not part of the public microservlet API.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
@UmlClassDiagram(diagram = DiagramJetty.class)
public class MicroservletJettyFilterPlugin extends BaseJettyFilterPlugin
{
    /** The Jetty holder for an instance of {@link JettyMicroservletFilter} */
    @UmlAggregation
    private final JettyMicroservletFilterHolder filterHolder;

    /**
     * @param application The REST application
     */
    public MicroservletJettyFilterPlugin(MicroserviceRestService application)
    {
        super(application.getClass().getSimpleName());

        application.listenTo(filterHolder = new JettyMicroservletFilterHolder(application));
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
    public void mount(MicroservletRestPath path, Microservlet<?, ?> microservlet)
    {
        filterHolder.mount(path, microservlet);
    }

    /**
     * Forwards requests to mount JAR files for alternate API versions to the filter holder
     */
    public void mount(MicroservletRestPath path, Resource jar, String commandLine, int port)
    {
        filterHolder.mount(path, jar, commandLine, port);
    }
}

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

package com.telenav.kivakit.web.wicket;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.web.jetty.resources.BaseFilterJettyPlugin;
import org.apache.wicket.protocol.http.WebApplication;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.servlet.FilterHolder;

import java.util.EnumSet;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static jakarta.servlet.DispatcherType.ASYNC;
import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static jakarta.servlet.DispatcherType.INCLUDE;
import static jakarta.servlet.DispatcherType.REQUEST;

/**
 * A Jetty filter for Apache Wicket applications. The Wicket {@link WebApplication} class is passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class WicketJettyPlugin extends BaseFilterJettyPlugin
{
    /** Wicket web application */
    private WebApplication application;

    /** Wicket-specific definition of a web application */
    private Class<? extends WebApplication> applicationClass;

    public WicketJettyPlugin(Class<? extends WebApplication> applicationClass)
    {
        super(applicationClass.getSimpleName());
        this.applicationClass = ensureNotNull(applicationClass);
    }

    public WicketJettyPlugin(WebApplication application)
    {
        super(application.getClass().getSimpleName());
        this.application = ensureNotNull(application);
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
    public FilterHolder holder()
    {
        return applicationClass != null
                ? new JettyWicketFilterHolder(applicationClass)
                : new JettyWicketFilterHolder(application);
    }
}

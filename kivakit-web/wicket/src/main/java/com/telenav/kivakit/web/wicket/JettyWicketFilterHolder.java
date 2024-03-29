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
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.servlet.FilterHolder;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Installs and configures the {@link WicketFilter} required to serve the given {@link WebApplication} class.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
class JettyWicketFilterHolder extends FilterHolder
{
    public JettyWicketFilterHolder(Class<? extends WebApplication> applicationClass)
    {
        setFilter(new WicketFilter());
        setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, applicationClass.getName());
        setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    }

    public JettyWicketFilterHolder(WebApplication application)
    {
        setFilter(new WicketFilter(application));
        setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    }
}

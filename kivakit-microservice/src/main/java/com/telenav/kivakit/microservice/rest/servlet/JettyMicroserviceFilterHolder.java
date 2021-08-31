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

package com.telenav.kivakit.microservice.rest.servlet;

import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.servlet.FilterHolder;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Installs and configures the {@link JettyMicroserviceFilter} required to handle REST requests.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class JettyMicroserviceFilterHolder extends FilterHolder
{
    public JettyMicroserviceFilterHolder(final MicroserviceRestApplication application)
    {
        setFilter(new JettyMicroserviceFilter(application));
    }
}

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

package com.telenav.kivakit.microservice.rest;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.microservice.rest.servlet.JettyMicroserviceFilter;
import com.telenav.kivakit.web.jersey.BaseRestApplication;
import com.telenav.kivakit.web.jersey.JerseyGsonSerializer;

import javax.ws.rs.ApplicationPath;

/**
 * REST interface for the KivaKit example microservice
 *
 * @author jonathanl (shibo)
 */
@ApplicationPath("/api")
public abstract class MicroserviceRestApplication extends BaseRestApplication
{
    private final JettyMicroserviceFilter filter = new JettyMicroserviceFilter(this);

    public MicroserviceRestApplication()
    {
        register(new JerseyGsonSerializer<>(gsonFactory()));
    }

    public abstract MicroserviceGsonFactory gsonFactory();

    /**
     * Mounts the given request method on the given path. Paths descend from the root of the server.
     */
    protected final void mount(String path, Class<? extends MicroserviceRestRequest<?>> request)
    {
        filter.mount("/api/" + Application.get().version() + "/" + path, request);
    }
}

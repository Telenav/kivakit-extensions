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

import com.google.gson.Gson;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.JettyMicroservlet;
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
    private final Microservice microservice;

    private JettyMicroservlet jettyMicroservlet;

    public MicroserviceRestApplication(Microservice microservice)
    {
        this.microservice = microservice;

        // Install the Jersey JSON serializer with the given Gson factory
        register(new JerseyGsonSerializer<>(gsonFactory()));
    }

    /**
     * @return Factory that can create a {@link Gson} instance for serializing JSON object
     */
    public abstract MicroserviceGsonFactory gsonFactory();

    /**
     * @return The microservice to which this rest application belongs
     */
    public Microservice microservice()
    {
        return microservice;
    }

    public void mount(String path, Class<Microservlet<?, ?>> microservletType)
    {
        try
        {
            var microservlet = microservletType
                    .getConstructor(Listener.class)
                    .newInstance(this);

            mount(path, microservlet);
        }
        catch (final Exception e)
        {
            problem("Couldn't construct converter: $", microservletType);
        }
    }

    public void mount(String path, Microservlet microservlet)
    {
        jettyMicroservlet.mount(path, microservlet);
    }
}

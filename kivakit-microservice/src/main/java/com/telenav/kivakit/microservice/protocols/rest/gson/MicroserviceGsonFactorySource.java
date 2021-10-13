package com.telenav.kivakit.microservice.protocols.rest.gson;

import com.google.gson.Gson;
import com.telenav.kivakit.microservice.internal.microservlet.rest.plugins.jetty.openapi.JettyOpenApiRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.serialization.json.GsonFactory;

/**
 * Interface that can be implemented by a {@link MicroservletResponse} object to provide a custom JSON response when
 * called during a REST request. This interface should only be necessary in special cases. For example, in {@link
 * JettyOpenApiRequest}, where a different {@link GsonFactory} is required.
 *
 * @author jonathanl (shibo)
 * @see JettyOpenApiRequest
 */
public interface MicroserviceGsonFactorySource
{
    default Gson gson()
    {
        return gsonFactory().newInstance();
    }

    /**
     * @return The {@link GsonFactory}
     */
    GsonFactory gsonFactory();
}

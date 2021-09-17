package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.rest.microservlet.cycle.MicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.JettyMicroservletFilter;
import com.telenav.kivakit.resource.resources.other.PropertyMap;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A microservlet responds to GET and POST requests made to the {@link JettyMicroservletFilter}.
 */
public class Microservlet<Request, Response> extends BaseComponent
{
    /** The type of the request object */
    private final Class<? extends Request> requestType;

    /** The type of the response object */
    private final Class<? extends Response> responseType;

    /** A thread local variable holding the request cycle for a given thread using this servlet */
    private final ThreadLocal<MicroservletRequestCycle> cycle = new ThreadLocal<>();

    /**
     * @param requestType The Request type
     * @param responseType The Response type
     */
    public Microservlet(Class<? extends Request> requestType, Class<? extends Response> responseType)
    {
        this.requestType = requestType;
        this.responseType = responseType;
    }

    /**
     * Attaches a request cycle to this servlet. The association is thread-local so that {@link Microservlet}s are
     * thread-safe but also have access to request cycle information.
     */
    public void attach(MicroservletRequestCycle cycle)
    {
        this.cycle.set(cycle);
    }

    /**
     * Detaches any attached request cycle from this microservlet
     */
    public void detach()
    {
        attach(null);
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onGet()
    {
        return unsupported("Microservlet $ does not support method: GET", objectName());
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onPost(Request request)
    {
        return unsupported("Microservlet $ does not support method: POST", objectName());
    }

    /**
     * @return The request object type
     */
    public Class<? extends Request> requestType()
    {
        return requestType;
    }

    /**
     * @return The response object type
     */
    public Class<? extends Response> responseType()
    {
        return responseType;
    }

    /**
     * @return The parameter value for the given key as an int
     */
    protected int asInt(String key)
    {
        return parameters().asInt(key);
    }

    /**
     * @return The parameter value for the given key as a long
     */
    protected long asLong(String key)
    {
        return parameters().asLong(key);
    }

    /**
     * @return The parameter value for the given key as an object
     */
    protected <T> T asObject(String key, Converter<String, T> converter)
    {
        return converter.convert(get(key));
    }

    /**
     * @return The parameter value for the given key as an object
     */
    protected <T> T asObject(String key, Class<Converter<String, T>> converterType)
    {
        try
        {
            var converter = converterType
                    .getConstructor(Listener.class)
                    .newInstance(this);

            return asObject(key, converter);
        }
        catch (final Exception e)
        {
            problem("Couldn't construct converter: $", converterType);
            return null;
        }
    }

    /**
     * @return The active request cycle for the calling thread
     */
    protected MicroservletRequestCycle cycle()
    {
        return cycle.get();
    }

    /**
     * @return The parameter value for the given key
     */
    protected String get(String key)
    {
        return parameters().get(key);
    }

    /**
     * @return The parameters for the request
     */
    protected PropertyMap parameters()
    {
        return cycle().request().parameters();
    }
}

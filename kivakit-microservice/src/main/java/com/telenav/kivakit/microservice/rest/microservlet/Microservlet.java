package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.resource.resources.other.PropertyMap;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A microservlet responds to GET and POST requests made to the {@link JettyMicroservletFilter}.
 */
public abstract class Microservlet<Request extends MicroservletRequest, Response extends MicroservletResponse> extends BaseComponent
{
    /** The type of the request object */
    private final Class<? extends Request> requestType;

    /** The type of the response object */
    private final Class<? extends Response> responseType;

    /** A thread local variable holding the request cycle for a given thread using this servlet */
    private final ThreadLocal<JettyMicroservletRequestCycle> cycle = new ThreadLocal<>();

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
    public void attach(JettyMicroservletRequestCycle cycle)
    {
        this.cycle.set(cycle);
    }

    /**
     * @return Description of what this microservice does, for OpenAPI
     */
    public String description()
    {
        return "No description available";
    }

    /**
     * Detaches any attached request cycle from this microservlet
     */
    public void detach()
    {
        attach(null);
    }

    @SuppressWarnings("unchecked")
    public Response get(final MicroservletRequest request)
    {
        return onGet((Request) request);
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onGet(Request request)
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

    @SuppressWarnings("unchecked")
    public Response post(final MicroservletRequest request)
    {
        return onPost((Request) request);
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
    protected JettyMicroservletRequestCycle cycle()
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

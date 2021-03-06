package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.lexakai.DiagramMicroservice;
import com.telenav.kivakit.microservice.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService.HttpMethod;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A microservlet responds to a request by implementing {@link #onRespond(MicroservletRequest)}. The response object
 * must be a subclass of {@link MicroservletResponse}.
 *
 * <p>The request and response type for a microservlet are provided by {@link #requestType()} and a {@link
 * #responseType()}. Parameters to a microservlet can be retrieved in a subclass with {@link #parameters()} and the
 * as*() methods.
 * </p>
 *
 * <p><b>IMPORTANT NOTE</b></p>
 * <p>
 * For most applications, it isn't necessary (or desirable) to directly subclass {@link Microservlet}. Instead a ({@link
 * MicroservletRequest} request handler should be mounted directly on a {@link MicroserviceRestService} with the {@link
 * MicroserviceRestService#mount(String, HttpMethod, Class)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see MicroservletRequest
 * @see MicroservletResponse
 * @see PropertyMap
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@UmlRelation(label = "attaches", referent = JettyMicroservletRequestCycle.class)
public abstract class Microservlet<Request extends MicroservletRequest, Response extends MicroservletResponse> extends BaseComponent implements Named
{
    /** The type of the request object */
    @UmlRelation(label = "references sub-class", referent = MicroservletRequest.class)
    private final Class<? extends Request> requestType;

    /** The type of the response object */
    @UmlRelation(label = "references sub-class", referent = MicroservletResponse.class)
    private final Class<? extends Response> responseType;

    /**
     * @param requestType The request type
     * @param responseType The response type
     */
    public Microservlet(Class<? extends Request> requestType, Class<? extends Response> responseType)
    {
        this.requestType = ensureNotNull(requestType);
        this.responseType = ensureNotNull(responseType);
    }

    /**
     * @return Description of what this microservice does, for OpenAPI
     */
    public String description()
    {
        return "No description available";
    }

    @Override
    public String name()
    {
        return "[Microservlet requestType = " + requestType().getSimpleName() + "]";
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onRespond(Request request)
    {
        return unsupported("Microservlet has no request handling implementation", objectName());
    }

    @SuppressWarnings("unchecked")
    public Response respond(MicroservletRequest request)
    {
        return onRespond((Request) request);
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
            var converter = listenTo(converterType
                    .getConstructor(Listener.class)
                    .newInstance(this));

            return asObject(key, converter);
        }
        catch (Exception e)
        {
            problem("Couldn't construct converter: $", converterType);
            return null;
        }
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
        return JettyMicroservletRequestCycle.cycle().request().parameters();
    }
}

package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.microservlet.rest.internal.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A microservlet responds to requests by implementing {@link #onRequest(MicroservletRequest)}. The response object must
 * be a subclass of {@link MicroservletResponse}.
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
 * MicroserviceRestService#mount(String, MicroserviceRestService.HttpMethod, Class)} or {@link
 * MicroserviceRestService#mount(String, Microservlet)} method.
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
    public Microservlet(final Class<? extends Request> requestType, final Class<? extends Response> responseType)
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
    public Response onRequest(final Request request)
    {
        return unsupported("Microservlet has no request handling implementation", objectName());
    }

    @SuppressWarnings("unchecked")
    public Response request(final MicroservletRequest request)
    {
        return onRequest((Request) request);
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
    protected int asInt(final String key)
    {
        return parameters().asInt(key);
    }

    /**
     * @return The parameter value for the given key as a long
     */
    protected long asLong(final String key)
    {
        return parameters().asLong(key);
    }

    /**
     * @return The parameter value for the given key as an object
     */
    protected <T> T asObject(final String key, final Converter<String, T> converter)
    {
        return converter.convert(get(key));
    }

    /**
     * @return The parameter value for the given key as an object
     */
    protected <T> T asObject(final String key, final Class<Converter<String, T>> converterType)
    {
        try
        {
            final var converter = listenTo(converterType
                    .getConstructor(Listener.class)
                    .newInstance(this));

            return asObject(key, converter);
        }
        catch (final Exception e)
        {
            problem("Couldn't construct converter: $", converterType);
            return null;
        }
    }

    /**
     * @return The parameter value for the given key
     */
    protected String get(final String key)
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

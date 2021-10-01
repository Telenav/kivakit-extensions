package com.telenav.kivakit.microservice.rest.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.conversion.Converter;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.JettyMicroservletRequestCycle;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletDeleteRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A microservlet responds to GET, POST and DELETE requests with {@link #onGet(MicroservletRequest)}, {@link
 * #onPost(MicroservletRequest)} and {@link #onDelete(MicroservletRequest)}. The response object must be a subclass of
 * {@link MicroservletResponse}.
 *
 * <p>The request and response type for a microservlet are provided by {@link #requestType()} and a {@link
 * #responseType()}. The set of supported HTTP methods (a {@link Microservlet} can implement more than one), are
 * available through {@link #supportedMethods()} and {@link #supports(MicroservletRequest.HttpMethod)}. Parameters to a
 * microservlet can be retrieved in a subclass with {@link #parameters()} and the as*() methods.
 * </p>
 *
 * <p><b>IMPORTANT NOTE</b></p>
 * <p>
 * For most applications, it isn't necessary (or desirable) to directly subclass {@link Microservlet}. Instead a request
 * handler ({@link MicroservletGetRequest}, {@link MicroservletPostRequest} or {@link MicroservletDeleteRequest}) should
 * be mounted directly on a {@link MicroserviceRestApplication} with the {@link MicroserviceRestApplication#mount(String,
 * Class)} or {@link MicroserviceRestApplication#mount(String, Microservlet)} method.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see MicroservletRequest
 * @see MicroservletResponse
 * @see MicroservletRequest.HttpMethod
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

    /** The set of methods that this microservlet supports */
    private ObjectSet<MicroservletRequest.HttpMethod> supportedMethods = ObjectSet.of(MicroservletRequest.HttpMethod.GET, MicroservletRequest.HttpMethod.POST, MicroservletRequest.HttpMethod.DELETE);

    /**
     * @param requestType The request type
     * @param responseType The response type
     */
    public Microservlet(final Class<? extends Request> requestType, final Class<? extends Response> responseType)
    {
        this.requestType = ensureNotNull(requestType);
        this.responseType = ensureNotNull(responseType);
    }

    @SuppressWarnings("unchecked")
    public Response delete(final MicroservletRequest request)
    {
        return onDelete((Request) request);
    }

    /**
     * @return Description of what this microservice does, for OpenAPI
     */
    public String description()
    {
        return "No description available";
    }

    @SuppressWarnings("unchecked")
    public Response get(final MicroservletRequest request)
    {
        return onGet((Request) request);
    }

    @Override
    public String name()
    {
        return "[Microservlet requestType = " + requestType().getSimpleName() + "]";
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onDelete(final Request request)
    {
        return unsupported("Microservlet $ does not support method: DELETE", objectName());
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onGet(final Request request)
    {
        return unsupported("Microservlet $ does not support method: GET", objectName());
    }

    /**
     * This method is unsupported unless overridden
     */
    public Response onPost(final Request request)
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
     * @param methods The set of methods that this microservlet supports
     */
    public Microservlet<Request, Response> supportedMethods(ObjectSet<MicroservletRequest.HttpMethod> methods)
    {
        this.supportedMethods = methods;
        return this;
    }

    public ObjectSet<MicroservletRequest.HttpMethod> supportedMethods()
    {
        return supportedMethods;
    }

    /**
     * @return True if the given HTTP method is supported
     */
    public boolean supports(MicroservletRequest.HttpMethod method)
    {
        return supportedMethods.contains(method);
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
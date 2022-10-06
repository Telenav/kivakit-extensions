package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.conversion.Converter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservice;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.JettyRestRequestCycle;
import com.telenav.kivakit.microservice.protocols.rest.http.RestRequestThread;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.network.http.HttpMethod;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A {@link Microservlet} subclass responds to a request by implementing {@link #onRespond(MicroservletRequest)}. The
 * response object returned must be a subclass of {@link MicroservletResponse}.
 *
 * <p>The request and response type for a microservlet are provided by {@link #requestType()} and a {@link
 * #responseType()}. Parameters to a microservlet can be retrieved in a subclass with the methods beginning with
 * "parameter".
 * </p>
 *
 * <p><b>IMPORTANT NOTE</b></p>
 * <p>
 * For most applications, it isn't necessary (or desirable) to directly subclass {@link Microservlet}. Instead a
 * ({@link MicroservletRequest} request handler should be mounted directly on a {@link RestService} with the method
 * {@link RestService#mount(String, HttpMethod, Class)}. Calling this method will install an anonymous subclass of
 * {@link Microservlet} that handles the request by dispatching it to the right request handler.
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #description()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #requestType()}</li>
 *     <li>{@link #responseType()}</li>
 * </ul>
 *
 * <p><b>Responding</b></p>
 *
 * <ul>
 *     <li>{@link #respond(MicroservletRequest)}</li>
 *     <li>{@link #onRespond(MicroservletRequest)}</li>
 * </ul>
 *
 * <p><b>Parameters</b></p>
 *
 * <ul>
 *     <li>{@link #parameterAsInt(String)}</li>
 *     <li>{@link #parameterAsLong(String)}</li>
 *     <li>{@link #parameterAsObject(String, Converter)}</li>
 *     <li>{@link #parameterAsObject(String, Class)}</li>
 *     <li>{@link #parameter(String)}</li>
 *     <li>{@link #parameters()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see MicroservletRequest
 * @see MicroservletResponse
 * @see PropertyMap
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramMicroservice.class)
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@UmlRelation(label = "attaches", referent = JettyRestRequestCycle.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class Microservlet<Request extends MicroservletRequest, Response extends MicroservletResponse> extends
        BaseComponent implements Named
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
     * Returns description of what this microservice does, for OpenAPI
     */
    public String description()
    {
        return "No description available";
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the request object type
     */
    public Class<? extends Request> requestType()
    {
        return requestType;
    }

    /**
     * Responds to the given request with a response
     */
    @SuppressWarnings("unchecked")
    public Response respond(MicroservletRequest request)
    {
        return onRespond((Request) request);
    }

    /**
     * Returns the response object type
     */
    public Class<? extends Response> responseType()
    {
        return responseType;
    }

    /**
     * Returns the parameter value for the given key
     */
    protected String parameter(String key)
    {
        return parameters().get(key);
    }

    /**
     * Returns the parameter value for the given key as an int
     */
    protected int parameterAsInt(String key)
    {
        return parameters().asIntegerObject(key);
    }

    /**
     * Returns the parameter value for the given key as a long
     */
    protected long parameterAsLong(String key)
    {
        return parameters().asLong(key);
    }

    /**
     * Returns the parameter value for the given key as an object
     */
    protected <T> T parameterAsObject(String key, Class<Converter<String, T>> converterType)
    {
        try
        {
            var converter = listenTo(converterType
                    .getConstructor(Listener.class)
                    .newInstance(this));

            return parameterAsObject(key, converter);
        }
        catch (Exception e)
        {
            problem("Couldn't construct converter: $", converterType);
            return null;
        }
    }

    /**
     * Returns the parameter value for the given key as an object
     */
    protected <T> T parameterAsObject(String key, Converter<String, T> converter)
    {
        return converter.convert(parameter(key));
    }

    /**
     * Returns the parameters for the request
     */
    protected PropertyMap parameters()
    {
        return RestRequestThread.requestCycle().restRequest().parameters();
    }
}

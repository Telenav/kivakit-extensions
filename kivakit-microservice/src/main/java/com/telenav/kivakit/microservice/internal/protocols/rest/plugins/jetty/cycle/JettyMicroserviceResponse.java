package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.serialization.json.GsonFactorySource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Microservice HTTP response abstraction.
 * </p>
 *
 * <p>
 * This response object contains an {@link HttpServletResponse} and belongs to a {@link JettyMicroservletRequestCycle}.
 * Errors can be reported to the response with:
 * <ul>
 *     <li>{@link #problem(int, String, Object...)}</li>
 *     <li>{@link #problem(int, Throwable, String, Object...)}</li>
 * </ul>
 * where the first parameter of each method is an HTTP status code.
 * </p>
 *
 * <p>
 * The {@link #writeObject(MicroservletResponse)} method validates the given {@link MicroservletResponse}
 * object before serializing it to JSON format using the {@link JettyMicroservletRequestCycle#gson()}
 * object obtained from the {@link MicroserviceRestService}. It then writes the JSON string to the
 * {@link HttpServletResponse}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public final class JettyMicroserviceResponse extends BaseComponent
{
    /** The request cycle to which this response belongs */
    @UmlAggregation
    private final JettyMicroservletRequestCycle cycle;

    /** Servlet response */
    private final HttpServletResponse httpResponse;

    /** Error messages that were reported to this response via {@link #problem(int, String, Object...)} */
    @Expose
    @JsonProperty
    @UmlAggregation
    @OpenApiIncludeMember(title = "Errors messages",
                          description = "A list of formatted error messages")
    private final MicroservletErrorResponse errors = new MicroservletErrorResponse();

    public JettyMicroserviceResponse(JettyMicroservletRequestCycle cycle, HttpServletResponse httpResponse)
    {
        this.cycle = cycle;
        this.httpResponse = httpResponse;

        errors.listenTo(this);

        status(SC_OK);
    }

    @Override
    public void onMessage(Message message)
    {
        if (status() == SC_OK && message.isWorseThanOrEqualTo(Problem.class))
        {
            status(SC_INTERNAL_SERVER_ERROR);
        }
    }

    public Problem problem(int status, String text, Object... arguments)
    {
        status(status);
        return super.problem(text, arguments);
    }

    public Problem problem(int status, Throwable exception, String text, Object... arguments)
    {
        status(status);
        return super.problem(exception, text + ": " + exception.getMessage(), arguments);
    }

    public int status()
    {
        return httpResponse.getStatus();
    }

    public void status(int status)
    {
        httpResponse.setStatus(status);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The version of the microservice that is responding to a request
     */
    @KivaKitIncludeProperty
    @OpenApiIncludeMember(title = "Version", description = "The microservice version from metadata")
    public Version version()
    {
        return cycle.application()
                .microservice()
                .metadata()
                .version();
    }

    /**
     * Writes the given response object to the servlet output stream in JSON format. If the request is invalid, or the
     * response is null or invalid, a {@link MicroservletErrorResponse} is sent with the captured error messages.
     *
     * @param response The response to write to the HTTP output stream
     */
    public void writeObject(MicroservletResponse response)
    {
        // Validate the response
        if (response != null)
        {
            // and if the response is invalid (any problems go into the response object),
            if (!response.isValid(response))
            {
                // then transmit a problem message.
                problem("Response object is invalid");
            }
        }

        // Get either the response JSON or the errors JSON if there are errors or there is no response,
        var json = errors.isEmpty() && response != null
                ? toJson(response)
                : toJson(errors);

        try
        {
            // then send the JSON to the servlet output stream.
            httpResponse.setContentType("application/json");
            httpResponse.getOutputStream().println(json);
        }
        catch (Exception e)
        {
            problem(e, "Unable to write JSON response to servlet output stream");
        }
    }

    /**
     * @param response The response object to be serialized
     * @return The object serialized into JSON format, using the application {@link GsonFactory}. This behavior can be
     * overridden by implementing {@link GsonFactorySource} to provide a custom {@link GsonFactory} for a given response
     * object.
     */
    private String toJson(Object response)
    {
        // We will serialize the response object itself by default.
        var objectToSerialize = response;

        // If the response object provides another object to serialize,
        if (response instanceof MicroserviceGsonObjectSource)
        {
            // then make that the object to serialize.
            objectToSerialize = ((MicroserviceGsonObjectSource) response).gsonObject();
        }

        // If the response object has a custom GsonFactory,
        if (response instanceof GsonFactorySource)
        {
            // use that to convert the response to JSON,
            return listenTo(((GsonFactorySource) response).gsonFactory()).gson().toJson(objectToSerialize);
        }
        else
        {
            // otherwise, use the GsonFactory, provided by the application through the request cycle.
            return cycle.gson().toJson(objectToSerialize);
        }
    }
}

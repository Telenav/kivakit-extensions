package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.MicroservletErrors;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
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
 * object obtained from the {@link MicroserviceRestApplication}. It then writes the JSON string to the
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
    @SuppressWarnings("FieldCanBeLocal")
    @UmlAggregation
    @OpenApiIncludeMember(title = "Errors messages",
                          description = "A list of formatted error messages")
    private final MicroservletErrors errors = new MicroservletErrors();

    public JettyMicroserviceResponse(final JettyMicroservletRequestCycle cycle, final HttpServletResponse httpResponse)
    {
        this.cycle = cycle;
        this.httpResponse = httpResponse;

        errors.listenTo(this);

        status(SC_OK);
    }

    @Override
    public void onMessage(final Message message)
    {
        if (status() == SC_OK && message.isWorseThanOrEqualTo(Problem.class))
        {
            status(SC_INTERNAL_SERVER_ERROR);
        }
    }

    public Problem problem(int status, final String text, final Object... arguments)
    {
        status(status);
        return super.problem(text, arguments);
    }

    public Problem problem(int status, Throwable exception, final String text, final Object... arguments)
    {
        status(status);
        return super.problem(exception, text + ": " + exception.getMessage(), arguments);
    }

    public int status()
    {
        return httpResponse.getStatus();
    }

    public void status(final int status)
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
     * Writes the given response object to the servlet output stream in JSON format.
     */
    public void writeObject(final MicroservletResponse response)
    {
        // If the response is invalid (any problems go into the response object),
        if (!response.isValid(response))
        {
            // then we have an invalid response
            problem("Response object is invalid");
        }

        try
        {
            // then output JSON for the object to the servlet output stream.
            this.httpResponse.setContentType("application/json");
            this.httpResponse.getOutputStream().println(response.toJson());
        }
        catch (final Exception e)
        {
            problem(e, "Unable to write JSON response to servlet output stream");
        }
    }
}

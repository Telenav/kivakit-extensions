package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.HttpProblem;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializer;
import com.telenav.kivakit.microservice.protocols.rest.http.RestSerializers;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import jakarta.servlet.http.HttpServletResponse;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.string.Brackets.unbracket;
import static com.telenav.kivakit.network.http.HttpStatus.BAD_REQUEST;
import static com.telenav.kivakit.network.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static com.telenav.kivakit.network.http.HttpStatus.OK;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Microservice HTTP response abstraction.
 * </p>
 *
 * <p>
 * This response object contains an {@link HttpServletResponse} and belongs to a {@link JettyRestRequestCycle}. Errors
 * can be reported to the response with:
 * <ul>
 *     <li>{@link #problem(HttpStatus, String, Object...)}</li>
 *     <li>{@link #problem(HttpStatus, Throwable, String, Object...)}</li>
 * </ul>
 * where the first parameter of each method is an HTTP status code.
 *
 * <p>
 * The {@link #writeResponse(MicroservletResponse)} method validates the given {@link MicroservletResponse}
 * object before serializing it using the {@link JettyRestRequestCycle#defaultRestSerializer()} ()}
 * object obtained from the {@link RestService}. It then writes the serialized text to the
 * {@link HttpServletResponse}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@UmlClassDiagram(diagram = DiagramJetty.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_SERVICE_PROVIDER)
public final class JettyRestResponse extends BaseComponent implements
    RestResponse,
    TryTrait
{
    /** The request cycle to which this response belongs */
    @UmlAggregation
    private final JettyRestRequestCycle cycle;

    /** Error messages that were reported to this response via {@link #problem(HttpStatus, String, Object...)} */
    @Expose
    @UmlAggregation
    private final MicroservletErrorResponse errors = new MicroservletErrorResponse();

    /** Servlet response */
    private final HttpServletResponse httpResponse;

    public JettyRestResponse(JettyRestRequestCycle cycle, HttpServletResponse httpResponse)
    {
        this.cycle = cycle;
        this.httpResponse = httpResponse;

        errors.listenTo(this);

        httpStatus(OK);
    }

    @Override
    public MicroservletErrorResponse errors()
    {
        return errors;
    }

    @Override
    public HttpServletResponse httpServletResponse()
    {
        return httpResponse;
    }

    @Override
    public HttpStatus httpStatus()
    {
        return HttpStatus.httpStatus(httpResponse.getStatus());
    }

    @Override
    public void httpStatus(HttpStatus status)
    {
        httpResponse.setStatus(status.code());
    }

    @Override
    public Problem problem(HttpStatus httpStatus, String text, Object... arguments)
    {
        return problem(httpStatus, null, text, arguments);
    }

    @Override
    public Problem problem(HttpStatus httpStatus, Throwable exception, String text, Object... arguments)
    {
        return transmit(new HttpProblem(httpStatus, exception, text, arguments));
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Writes the given response object to the servlet output stream in text format. If the request is invalid, or the
     * response is null or invalid, a {@link MicroservletErrorResponse} is sent with the captured error messages.
     *
     * @param response The response to write to the HTTP output stream
     */
    @Override
    public void writeResponse(MicroservletResponse response)
    {
        // If there is a response,
        if (response != null)
        {
            // and it is valid (any problems go into the response object),
            if (response.isValid(response))
            {
                // get the response type,
                var responseType = cycle.restRequest()
                    .parameters()
                    .getOrDefault("response-type", "always-okay");

                // set the status accordingly,
                switch (responseType)
                {
                    case "http-status" -> httpStatus(errors.httpStatus());
                    case "always-okay" -> httpStatus(OK);
                    default ->
                        problem(BAD_REQUEST, "Response-type must be 'http-status', or 'always-okay', if not omitted");
                }

                // and return the response content.
                writeContent(response);
            }
            else
            {
                // otherwise, the response is invalid.
                problem(INTERNAL_SERVER_ERROR, "Internal error: Response object is invalid");
            }
        }
        else
        {
            problem(INTERNAL_SERVER_ERROR, "Internal error: Response object is missing");
        }
    }

    /**
     * Returns the appropriate {@link RestSerializer} for the given response type. If the type itself does not define
     * <i>public static RestSerializer restSerializer()</i>, then the return value of {@link #defaultRestSerializer()}
     * method is used.
     *
     * @param responseType The response type
     * @return The {@link RestSerializer}
     */
    private <Request extends MicroservletRequest, Response extends MicroservletResponse>
    RestSerializer<Request, Response> restSerializer(Class<Response> responseType)
    {
        RestSerializer<Request, Response> serializer = RestSerializers.restSerializer(responseType);
        return serializer == null ? defaultRestSerializer() : serializer;
    }

    /**
     * Serializes and writes the given response to the servlet output stream
     *
     * @param response The response to write
     */
    @SuppressWarnings("unchecked")
    private <Request extends MicroservletRequest, Response extends MicroservletResponse> void writeContent(
        Response response)
    {
        tryCatch(() ->
        {
            // Get the serializer for the response type,
            RestSerializer<Request, Response> serializer = restSerializer((Class<Response>) response.getClass());

            // set the content type,
            httpResponse.setContentType(serializer.contentType());

            // and send the serialized response to the servlet output stream.
            var out = httpResponse.getOutputStream();

            if (serializer.contentType().equals("application/json"))
            {
                out.println("{");
                out.println(unbracket(serializer.serializeResponse(response)));
                out.println(",");
                out.println(unbracket(serializer.serializeErrors(errors)));
                out.println("}");
            }
            else if (serializer.contentType().equals("text/yaml"))
            {
                out.println(serializer.serializeResponse(response));
            }
            else
            {
                unsupported("Unsupported content type");
            }
        }, "Unable to write content response");
    }
}

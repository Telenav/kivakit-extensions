package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.HttpProblem;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import jakarta.servlet.http.HttpServletResponse;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
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
 * object before serializing it to JSON format using the {@link JettyRestRequestCycle#gson()}
 * object obtained from the {@link RestService}. It then writes the JSON string to the
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
public final class JettyRestResponse extends BaseComponent
    implements RestResponse
{
    /** The request cycle to which this response belongs */
    @UmlAggregation
    private final JettyRestRequestCycle cycle;

    /** Error messages that were reported to this response via {@link #problem(HttpStatus, String, Object...)} */
    @Expose
    @UmlAggregation
    @OpenApiIncludeMember(title = "Errors messages",
                          description = "A list of formatted error messages")
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
     * Writes the given response object to the servlet output stream in JSON format. If the request is invalid, or the
     * response is null or invalid, a {@link MicroservletErrorResponse} is sent with the captured error messages.
     *
     * @param response The response to write to the HTTP output stream
     */
    @Override
    public void writeResponse(MicroservletResponse response)
    {
        // Validate the response
        if (response != null)
        {
            // and if the response is invalid (any problems go into the response object),
            if (response.isValid(response))
            {
                var responseType = cycle.restRequest()
                    .parameters()
                    .getOrDefault("response-type", "always-okay");

                var json = new JsonArray();
                json.add(toJson(response));
                if (errors.isEmpty())
                {
                    json.add(toJson(errors));
                }

                switch (responseType)
                {
                    case "http-status" -> httpStatus(errors.httpStatus());
                    case "always-okay" -> httpStatus(OK);
                    default ->
                        problem(BAD_REQUEST, "Response-type must be 'http-status', or 'always-okay', if not omitted");
                }

                writeJson(response, json);
            }
            else
            {
                // then transmit a problem message.
                problem(INTERNAL_SERVER_ERROR, "Internal error: Response object is invalid");
            }
        }
        else
        {
            problem(INTERNAL_SERVER_ERROR, "Internal error: Response object is invalid");
        }
    }

    private void writeJson(MicroservletResponse response, JsonElement json)
    {
        if (json != null)
        {
            try
            {
                // Set the header to signal JSON content,
                httpResponse.setContentType("application/json");

                // then send the JSON to the servlet output stream.
                var out = httpResponse.getOutputStream();
                out.println(gson(response).toJson(json));
            }
            catch (Exception e)
            {
                problem(e, "Unable to write response to servlet output stream");
            }
        }
    }
}

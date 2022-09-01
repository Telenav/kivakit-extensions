package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramJetty;
import com.telenav.kivakit.microservice.microservlet.MicroservletErrorResponse;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonObjectSource;
import com.telenav.kivakit.microservice.protocols.rest.http.HttpProblem;
import com.telenav.kivakit.microservice.protocols.rest.http.RestResponse;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.network.http.HttpStatus;
import com.telenav.kivakit.serialization.gson.factory.GsonFactory;
import com.telenav.kivakit.serialization.gson.factory.GsonFactorySource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import javax.servlet.http.HttpServletResponse;

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
 * </p>
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

        httpStatus(HttpStatus.OK);
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

    /**
     * @param response The response object to be serialized
     * @return The object serialized into JSON format, using the application {@link GsonFactory}. This behavior can be
     * overridden by implementing {@link GsonFactorySource} to provide a custom {@link GsonFactory} for a given response
     * object.
     */
    @Override
    public String toJson(Object response)
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

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The version of the microservice that is responding to a request
     */
    @Override
    @KivaKitIncludeProperty
    @OpenApiIncludeMember(title = "Version", description = "The microservice version from metadata")
    public Version version()
    {
        return cycle.restService()
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
    @Override
    public void writeResponse(MicroservletResponse response)
    {
        var responseWritten = false;

        // Validate the response
        if (response != null)
        {
            // and if the response is invalid (any problems go into the response object),
            if (response.isValid(response))
            {
                var responseType = cycle.restRequest().parameters().getOrDefault("response-type", "always-okay");

                switch (responseType)
                {
                    case "http-status":
                        var json = errors.isEmpty()
                                ? toJson(response)
                                : toJson(errors);
                        writeResponse(json);
                        httpStatus(errors.httpStatus());
                        responseWritten = true;
                        break;

                    case "always-okay":
                        writeResponse("{");
                        writeResponse(toJson(response));
                        writeResponse(toJson(errors));
                        writeResponse("}");
                        httpStatus(HttpStatus.OK);
                        responseWritten = true;
                        break;

                    default:
                        problem(HttpStatus.BAD_REQUEST, "Response-type must be 'http-status', or 'always-okay', if not omitted");
                        break;
                }
            }
            else
            {
                // then transmit a problem message.
                problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error: Response object is invalid");
            }
        }
        else
        {
            problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error: Response object is invalid");
        }

        if (!responseWritten)
        {
            writeResponse(toJson(response));
        }
    }

    /**
     * Writes the given string to the response as application/json
     *
     * @param json The JSON to write
     */
    private void writeResponse(final String json)
    {
        try
        {
            // then send the JSON to the servlet output stream.
            httpResponse.setContentType("application/json");
            httpResponse.getOutputStream().println(json);
        }
        catch (Exception e)
        {
            problem(e, "Unable to write response to servlet output stream");
        }
    }
}

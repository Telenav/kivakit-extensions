package com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.messages.Result;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.servlet.http.HttpServletResponse;

/**
 * Microservice response abstraction, holding a problem and a result object. The {@link #onTransmitting(Transmittable)}
 * method captures any problems that are broadcast to this response object.
 *
 * @author jonathanl (shibo)
 */
public final class JettyMicroserviceResponse extends BaseComponent
{
    /** Any problem that occurred */
    @KivaKitExcludeProperty
    private Problem problem;

    /** Response object */
    private Object object;

    /** The request cycle to which this response belongs */
    private final JettyMicroservletRequestCycle cycle;

    /** Servlet response */
    private final HttpServletResponse response;

    public JettyMicroserviceResponse(JettyMicroservletRequestCycle cycle, HttpServletResponse response)
    {
        this.cycle = cycle;
        this.response = response;
    }

    /**
     * @return This response as a {@link Result} object
     */
    public Result<Object> asResult()
    {
        return problem != null
                ? Result.failed(problem)
                : Result.succeeded(object);
    }

    /**
     * Capture any problem that might be sent to this response object
     */
    @Override
    public void onTransmitting(final Transmittable message)
    {
        if (message instanceof Problem)
        {
            problem = (Problem) message;
        }
    }

    /**
     * @return Any problem, or null if there is no problem
     */
    @KivaKitIncludeProperty
    @Schema(description = "A description of any problem that might have occurred")
    public Problem problem()
    {
        return problem;
    }

    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * @return The version of the microservice that is responding to a request
     */
    @KivaKitIncludeProperty
    @Schema(description = "The microservice version")
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
    public void writeObject(final Object object)
    {
        // If the object can be validated,
        if (object instanceof Validatable)
        {
            // then validate it.
            final var validatable = (Validatable) object;
            validatable.validator().validate(this);
        }

        // If there is no problem with the response,
        if (problem() == null)
        {
            try
            {
                // then output JSON for the object to the servlet output stream.
                var out = this.response.getOutputStream();
                out.println(cycle.gson().toJson(object));
            }
            catch (Exception e)
            {
                problem(e, "Unable to write JSON response to servlet output stream");
            }
        }
    }
}

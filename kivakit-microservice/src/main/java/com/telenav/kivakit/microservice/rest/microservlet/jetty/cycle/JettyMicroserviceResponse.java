package com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.Validatable;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
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
    /** The request cycle to which this response belongs */
    private final JettyMicroservletRequestCycle cycle;

    /** Servlet response */
    private final HttpServletResponse response;

    public JettyMicroserviceResponse(JettyMicroservletRequestCycle cycle, HttpServletResponse response)
    {
        this.cycle = cycle;
        this.response = response;
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

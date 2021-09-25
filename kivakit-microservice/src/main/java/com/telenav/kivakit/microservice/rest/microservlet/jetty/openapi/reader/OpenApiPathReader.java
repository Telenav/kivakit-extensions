package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.JettyOpenApiRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletErrors;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.wicket.util.string.Strings;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Reads the OpenAPI Paths from {@link JettyMicroservletFilter} mounted requests.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class OpenApiPathReader extends BaseComponent
{
    /**
     * @return Path models for all mounted paths in
     */
    public Paths read()
    {
        final var paths = new Paths();

        // Got through each mount path that the filter has,
        var filter = require(JettyMicroservletFilter.class);
        for (final var path : filter.paths())
        {
            // and add a PathItem to the list of paths.
            final var microservlet = filter.microservlet(path);
            if (microservlet != null)
            {
                paths.addPathItem(path.path().join(), newPathItem(microservlet));
            }
            else
            {
                problem("Unable to locate microservlet $ $", path.method(), path.path());
            }
        }

        return paths;
    }

    /**
     * @return True if the request type should be ignored
     */
    private boolean ignoreRequestType(final Class<? extends MicroservletRequest> requestType)
    {
        return JettyOpenApiRequest.class.isAssignableFrom(requestType);
    }

    /**
     * An OpenAPI {@link Operation} model for the given method
     *
     * @param methodName The name of the method
     * @param requestType The request type
     * @param responseType The response type
     * @return The OpenAPI {@link Operation} model
     */
    private Operation newOperation(final String methodName,
                                   final Class<? extends MicroservletRequest> requestType,
                                   final Class<? extends MicroservletResponse> responseType)
    {
        // then create an operation and populate it with the summary and description of the request,
        final var operation = new Operation();
        var annotationReader = require(OpenApiAnnotationReader.class);
        operation.summary(annotationReader.readAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::summary));
        operation.description(annotationReader.readAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::description));
        operation.operationId(annotationReader.readAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::operationId));
        operation.deprecated(Strings.isTrue(annotationReader.readAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, op -> Boolean.toString(op.deprecated()))));

        // add success and error responses,
        operation.responses(new ApiResponses()
                .addApiResponse("200", newResponseSuccess(responseType))
                .addApiResponse("403", newResponseItem("Forbidden", null))
                .addApiResponse("404", newResponseItem("Not Found", null))
                .addApiResponse("500", newResponseItem("Server Error", schemaError())));

        operation.requestBody(new RequestBody().content(newRequestContent(requestType)));

        // and return the operation
        return operation;
    }

    /**
     * Creates a {@link PathItem} for a {@link Microservlet}.
     *
     * @param microservlet The microservlet
     * @return The {@link PathItem}
     */
    private PathItem newPathItem(final Microservlet<?, ?> microservlet)
    {
        ensureNotNull(microservlet);

        // Create the path item and give it the microservlet's description,
        final var item = new PathItem();
        item.description(microservlet.description());

        // get the request and response types,
        final var requestType = microservlet.requestType();
        final var responseType = microservlet.responseType();

        if (!ignoreRequestType(requestType))
        {
            // add them to the set of schema models,
            require(OpenApiSchemaReader.class)
                    .add(requestType)
                    .add(responseType);

            // and if it's a get request,
            if (MicroservletGetRequest.class.isAssignableFrom(requestType))
            {
                // then add a get operation description,
                item.get(newOperation("onGet", requestType, responseType));
            }

            // and if it's a post request,
            if (MicroservletPostRequest.class.isAssignableFrom(requestType))
            {
                // add a post operation description.
                item.get(newOperation("onPost", requestType, responseType));
            }
            return item;
        }
        else
        {
            return null;
        }
    }

    /**
     * @return A {@link Content} object for the given request type
     */
    private Content newRequestContent(final Class<? extends MicroservletRequest> requestType)
    {
        // Add the request type to the set of models,
        require(OpenApiSchemaReader.class).add(requestType);

        // then return an application/json content object that refers to the request type's schema.
        return new Content()
                .addMediaType("application/json", new MediaType()
                        .schema(new Schema<>()
                                .$ref("#/components/schemas/" + requestType.getSimpleName())));
    }

    /**
     * @return A new {@link ApiResponse} for the given description and schema.
     */
    private ApiResponse newResponseItem(final String description, final Schema<?> schema)
    {
        var item = new ApiResponse().description(description);
        if (schema != null)
        {
            item.content(new Content().addMediaType("application/json", new MediaType().schema(schema)));
            item.$ref("#/components/schemas/" + schema.getName());
        }
        return item;
    }

    /**
     * @return An {@link ApiResponse} description for the given response type.
     */
    private ApiResponse newResponseSuccess(final Class<? extends MicroservletResponse> responseType)
    {
        // Add the response type to the set of models,
        require(OpenApiSchemaReader.class).add(ensureNotNull(responseType));

        // and return a 200 response with the schema for the response type.
        return newResponseItem("200", schema(responseType));
    }

    /**
     * @return The {@link Schema} for the given model
     */
    private Schema<?> schema(final Class<?> model)
    {
        return require(OpenApiSchemaReader.class).read(model);
    }

    /**
     * @return The {@link Schema} for {@link MicroservletErrors}s.
     */
    private Schema<?> schemaError()
    {
        return schema(MicroservletErrors.class);
    }
}

package com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.JettyOpenApiRequest;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiRequestHandler;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

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
        final var filter = require(JettyMicroservletFilter.class);
        for (final var path : filter.paths())
        {
            // and add a PathItem to the list of paths.
            final var microservlet = filter.microservlet(path);
            if (microservlet != null)
            {
                paths.addPathItem(path.resolvedPath().join(), newPathItem(microservlet));
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
        final var annotationReader = new OpenApiAnnotationReader();
        operation.summary(annotationReader.readAnnotationValue(requestType, methodName, OpenApiRequestHandler.class, OpenApiRequestHandler::summary));
        operation.description(annotationReader.readAnnotationValue(requestType, methodName, OpenApiRequestHandler.class, OpenApiRequestHandler::description));

        // add success and error responses,
        operation.responses(new ApiResponses()
                .addApiResponse("200", newResponseSuccess(responseType))
                .addApiResponse("403", newResponseItem("Forbidden", null))
                .addApiResponse("404", newResponseItem("Not Found", null))
                .addApiResponse("500", newResponseItem("Server Error", require(OpenApiSchemaReader.class).schemaError())));

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
                    .addModelToRead(Type.forClass(requestType))
                    .addModelToRead(Type.forClass(responseType));

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
                item.post(newOperation("onPost", requestType, responseType));
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
        require(OpenApiSchemaReader.class).addModelToRead(Type.forClass(requestType));

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
        final var item = new ApiResponse().description(description);
        if (schema != null)
        {
            item.content(new Content().addMediaType("application/json", new MediaType().schema(schema)));
        }
        return item;
    }

    /**
     * @return An {@link ApiResponse} description for the given response type.
     */
    private ApiResponse newResponseSuccess(final Class<? extends MicroservletResponse> responseType)
    {
        // Add the response type to the set of models,
        require(OpenApiSchemaReader.class)
                .addModelToRead(ensureNotNull(Type.forClass(responseType)));

        // and return a 200 response with the schema for the response type.
        return newResponseItem("Success", new Schema<>().$ref(new ReferenceResolver().reference(Type.forClass(responseType))));
    }
}

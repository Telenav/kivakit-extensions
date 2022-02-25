package com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.OpenApiJsonRequest;
import com.telenav.kivakit.microservice.microservlet.Microservlet;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.protocols.rest.MicroservletRestPath;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiRequestHandler;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

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
        var paths = new Paths();

        // Got through each mount path that the filter has,
        var filter = require(JettyMicroservletFilter.class);
        for (var path : new ObjectList<>(filter.microservletPaths()).sorted())
        {
            // and add a PathItem to the list of paths.
            var mounted = filter.microservlet(path);
            if (mounted != null)
            {
                paths.addPathItem(path.resolvedPath().join(), newPathItem(path, mounted.microservlet()));
            }
            else
            {
                problem("Unable to locate microservlet $ $", path.method(), path.path());
            }
        }

        return paths;
    }

    private void addErrorResponse(ApiResponses responses, int code, String description)
    {
        responses.addApiResponse(Integer.toString(code), newResponseItem(description, require(OpenApiSchemaReader.class).schemaError()));
    }

    /**
     * @return True if the request type should be ignored
     */
    private boolean ignoreRequestType(Class<? extends MicroservletRequest> requestType)
    {
        return OpenApiJsonRequest.class.isAssignableFrom(requestType);
    }

    /**
     * An OpenAPI {@link Operation} model for the given method
     *
     * @param requestType The request type
     * @param responseType The response type
     * @param path Path to REST operation
     * @return The OpenAPI {@link Operation} model
     */
    private Operation newOperation(Class<? extends MicroservletRequest> requestType,
                                   Class<? extends MicroservletResponse> responseType,
                                   MicroservletRestPath path)
    {
        // Create operation,
        var operation = new Operation();
        var annotationReader = new OpenApiAnnotationReader();
        var operationId = Strip.leading(path.key().replaceAll("/", "-"), "-");

        operation.operationId(operationId);
        operation.summary(annotationReader.readAnnotationString(requestType, "onRespond", OpenApiRequestHandler.class, OpenApiRequestHandler::summary));
        operation.description(annotationReader.readAnnotationString(requestType, "onRespond", OpenApiRequestHandler.class, OpenApiRequestHandler::description));
        operation.tags(annotationReader.readAnnotationStringList(requestType, "onRespond", OpenApiRequestHandler.class, OpenApiRequestHandler::tags));

        if (operation.getTags() == null || operation.getTags().isEmpty())
        {
            operation.tags(List.of("api version " + path.version()));
        }

        // add success and error responses,
        var responses = new ApiResponses()
                .addApiResponse(Integer.toString(SC_OK), newResponseSuccess(responseType))
                .addApiResponse(Integer.toString(SC_FORBIDDEN), newResponseItem("Forbidden", null))
                .addApiResponse(Integer.toString(SC_NOT_FOUND), newResponseItem("Not Found", null));

        addErrorResponse(responses, SC_INTERNAL_SERVER_ERROR, "Server Error");
        addErrorResponse(responses, SC_BAD_REQUEST, "Invalid Request");

        operation.responses(responses);

        // and request body,
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
    private PathItem newPathItem(
            MicroservletRestPath path,
            Microservlet<?, ?> microservlet)
    {
        ensureNotNull(microservlet);

        // Create the path item and give it the microservlet's description,
        var item = new PathItem();
        item.description(microservlet.description());

        // get the request and response types,
        var requestType = microservlet.requestType();
        var responseType = microservlet.responseType();

        if (!ignoreRequestType(requestType))
        {
            // add them to the set of schema models,
            require(OpenApiSchemaReader.class)
                    .addModelToRead(Type.forClass(requestType))
                    .addModelToRead(Type.forClass(responseType));

            // and add an item based on the HTTP method type
            switch (path.httpMethod())
            {
                case GET:
                    item.get(newOperation(requestType, responseType, path));
                    break;

                case POST:
                    item.post(newOperation(requestType, responseType, path));
                    break;

                case DELETE:
                    item.delete(newOperation(requestType, responseType, path));
                    break;
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
    private Content newRequestContent(Class<? extends MicroservletRequest> requestType)
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
    private ApiResponse newResponseItem(String description, Schema<?> schema)
    {
        var item = new ApiResponse().description(description);
        if (schema != null)
        {
            item.content(new Content().addMediaType("application/json", new MediaType().schema(schema)));
        }
        return item;
    }

    /**
     * @return An {@link ApiResponse} description for the given response type.
     */
    private ApiResponse newResponseSuccess(Class<? extends MicroservletResponse> responseType)
    {
        // Add the response type to the set of models,
        require(OpenApiSchemaReader.class)
                .addModelToRead(ensureNotNull(Type.forClass(responseType)));

        // and return a 200 response with the schema for the response type.
        return newResponseItem("Success", new Schema<>().$ref(new ReferenceResolver().reference(Type.forClass(responseType))));
    }
}

package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi;

import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramJetty;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletErrors;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.requests.MicroservletPostRequest;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.wicket.util.string.Strings;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * <b>Not public API</b>
 * <p>
 * A {@link MicroservletGetRequest} request that produces an {@link OpenAPI} definition for a {@link
 * MicroservletJettyFilterPlugin}.
 */
@UmlClassDiagram(diagram = DiagramJetty.class)
public class JettyOpenApiRequest extends MicroservletGetRequest
{
    /**
     * Response to OpenAPI request
     */
    public static class Response extends MicroservletResponse
    {
        @SuppressWarnings("FieldCanBeLocal")
        private OpenAPI openAPI;

        public Response()
        {
        }

        public Response(JettyOpenApiRequest request)
        {
            openAPI = request.openApi();
        }

        public OpenAPI openAPI()
        {
            return openAPI;
        }

        @Override
        public String toJson()
        {
            return gson().toJson(openAPI);
        }
    }

    /** Models for which to make schemas */
    private final Set<Class<?>> models = new HashSet<>();

    public JettyOpenApiRequest()
    {
    }

    /**
     * @return Responds to a GET request with the OpenAPI definition for the {@link MicroserviceRestApplication}.
     */
    @Override
    public MicroservletResponse onGet()
    {
        return new Response(this);
    }

    /**
     * @return An OpenAPI definition for the {@link JettyMicroservletFilter} passed to the constructor
     */
    public OpenAPI openApi()
    {
        final var api = initialize(new OpenAPI());

        // Add metadata
        addInfo(api);

        // Add paths to mounted items
        addPaths(api);

        // Add schemas
        addSchemas(api);

        return api;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MicroservletResponse> responseType()
    {
        return Response.class;
    }

    /**
     * Adds a Swagger {@link Info} object to the given {@link OpenAPI} object. The {@link Info} object is populated from
     * {@link Microservice} metadata by {@link MicroserviceRestApplication#openApiInfo()}.
     */
    private void addInfo(final OpenAPI api)
    {
        api.info(filter().restApplication().openApiInfo());
    }

    /**
     * Adds each path that is mounted on the {@link JettyMicroservletFilter}. The {@link PathItem} for each path is
     * resolved by {@link #pathItem(Microservlet)}.
     */
    private void addPaths(final OpenAPI api)
    {
        final var paths = new Paths();

        // Got through each mount path that the filter has,
        for (final var path : filter().paths())
        {
            // and add a PathItem to the list of paths.
            final var microservlet = filter().microservlet(path);
            if (microservlet != null)
            {
                paths.addPathItem(path.path().join(), pathItem(microservlet));
            }
            else
            {
                problem(SC_INTERNAL_SERVER_ERROR, "Unable to locate microservlet $ $", path.method(), path.path());
            }
        }

        // Add the paths to the OpenAPI object
        api.paths(paths);
    }

    /**
     * Add {@link Schema}s for all the models in {@link #models} set. This set is populated during {@link
     * #addPaths(OpenAPI)}.
     */
    private void addSchemas(final OpenAPI api)
    {
        final var schemas = api.getComponents().getSchemas();
        for (final var model : models)
        {
            if (model != null)
            {
                schemas.putAll(ModelConverters.getInstance().read(model));
            }
            else
            {
                problem(SC_INTERNAL_SERVER_ERROR, "Model is null");
            }
        }
    }

    private JettyMicroservletFilter filter()
    {
        return require(JettyMicroservletFilter.class);
    }

    private OpenAPI initialize(final OpenAPI api)
    {
        api.components(new Components().schemas(new HashMap<>()));
        return api;
    }

    private <T extends Annotation> String methodAnnotationValue(
            final Class<? extends MicroservletRequest> requestType,
            final String method,
            final Class<T> annotationClass,
            final Function<T, String> function)
    {
        try
        {
            final var annotation = requestType
                    .getMethod(method)
                    .getAnnotation(annotationClass);

            return annotation == null ? null : function.apply(annotation);
        }
        catch (final NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return null;
        }
    }

    /**
     * @return A new {@link ApiResponse} for the given description and schema.
     */
    private ApiResponse newResponse(final String description, final Schema<?> schema)
    {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("application/json", new MediaType().schema(schema)));
    }

    private Operation operation(final String methodName,
                                final Class<? extends MicroservletRequest> requestType,
                                final Class<? extends MicroservletResponse> responseType)
    {
        // then create an operation and populate it with the summary and description of the request,
        final var operation = new Operation();
        operation.summary(methodAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::summary));
        operation.description(methodAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::description));
        operation.operationId(methodAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::operationId));
        operation.deprecated(Strings.isTrue(methodAnnotationValue(requestType, methodName, io.swagger.v3.oas.annotations.Operation.class, op -> Boolean.toString(op.deprecated()))));

        // add success and error responses,
        operation.responses(new ApiResponses()
                .addApiResponse("200", responseSuccess(responseType))
                .addApiResponse("403", newResponse("Forbidden", null))
                .addApiResponse("404", newResponse("Not Found", null))
                .addApiResponse("500", newResponse("Server Error", schemaError())));

        operation.requestBody(new RequestBody().content(requestContent(requestType)));

        // and return the operation
        return operation;
    }

    private Operation operationGet(final Class<? extends MicroservletGetRequest> requestType,
                                   final Class<? extends MicroservletResponse> responseType)
    {
        return operation("onGet", requestType, responseType);
    }

    /**
     * @param requestType The type of the POSTed JSON request object
     * @param responseType The type of the JSON response object
     * @return The OpenAPI {@link Operation} describing this post operation
     */
    private Operation operationPost(final Class<? extends MicroservletPostRequest> requestType,
                                    final Class<? extends MicroservletResponse> responseType)
    {
        return operation("onPost", requestType, responseType);
    }

    /**
     * Creates a {@link PathItem} for a {@link Microservlet}.
     *
     * @param microservlet The microservlet
     * @return The {@link PathItem}
     */
    @SuppressWarnings("unchecked")
    private PathItem pathItem(final Microservlet<?, ?> microservlet)
    {
        ensureNotNull(microservlet);

        // Create the path item and give it the microservlet's description,
        final var item = new PathItem();
        item.description(microservlet.description());

        // get the request and response types,
        final var requestType = microservlet.requestType();
        final var responseType = microservlet.responseType();

        // add them to the set of models,
        models.add(ensureNotNull(requestType));
        models.add(ensureNotNull(responseType));

        // and if it's a get request,
        if (requestType.isAssignableFrom(MicroservletGetRequest.class))
        {
            // then add a get operation description,
            item.get(operationGet((Class<? extends MicroservletGetRequest>) requestType, responseType));
        }

        // and if it's a post request,
        if (requestType.isAssignableFrom(MicroservletPostRequest.class))
        {
            // add a post operation description.
            item.post(operationPost((Class<? extends MicroservletPostRequest>) requestType, responseType));
        }

        return item;
    }

    /**
     * @return A {@link Content} object for the given request type
     */
    private Content requestContent(final Class<? extends MicroservletRequest> requestType)
    {
        // Add the request type to the set of models,
        models.add(ensureNotNull(requestType));

        // then return an application/json content object that refers to the request type's schema.
        return new Content()
                .addMediaType("application/json", new MediaType()
                        .schema(new Schema<>()
                                .$ref(requestType.getSimpleName())));
    }

    /**
     * @return An {@link ApiResponse} description for the given response type.
     */
    private ApiResponse responseSuccess(final Class<? extends MicroservletResponse> responseType)
    {
        // Add the response type to the set of models,
        models.add(ensureNotNull(responseType));

        // and return a 200 response with the schema for the response type.
        return newResponse("200", schema(responseType));
    }

    /**
     * @return The {@link Schema} for the given model
     */
    @SuppressWarnings("rawtypes")
    private Schema<?> schema(final Class<?> model)
    {
        // Get the schemas for the model,
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(model);

        // and if there is only one schema,
        if (schemas.size() == 1)
        {
            // return it,
            return schemas.values().iterator().next();
        }

        // otherwise, there were no schemas, or more than one.
        problem("Could not load schema: $", model);
        return null;
    }

    /**
     * @return The {@link Schema} for {@link MicroservletErrors}s.
     */
    private Schema<?> schemaError()
    {
        // Add the model to the set of models,
        models.add(MicroservletErrors.class);

        // and return a schema that references the MicroservletErrors schema.
        return new Schema<>()
                .name("MicroservletErrors")
                .$ref("#/components/schemas/MicroservletErrors");
    }
}

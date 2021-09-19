package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi;

import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.Microservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.JettyMicroservlet;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.JettyMicroservletFilter;
import com.telenav.kivakit.microservice.rest.microservlet.model.BaseMicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletErrors;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletRequest;
import com.telenav.kivakit.microservice.rest.microservlet.model.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.model.methods.MicroservletGet;
import com.telenav.kivakit.microservice.rest.microservlet.model.methods.MicroservletPost;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A {@link MicroservletGet} request that produces an {@link OpenAPI} definition for a {@link JettyMicroservlet}.
 */
public class JettyOpenApiRequest extends MicroservletGet
{
    /**
     * Response to OpenAPI request
     */
    private class Response extends BaseMicroservletResponse
    {
        @SuppressWarnings("FieldCanBeLocal")
        private final OpenAPI openAPI;

        public Response()
        {
            openAPI = openApi();
        }
    }

    /** The Java Servlet API filter */
    private final JettyMicroservletFilter filter;

    /** Models for which to make schemas */
    private final Set<Class<?>> models = new HashSet<>();

    public JettyOpenApiRequest(JettyMicroservletFilter filter)
    {
        this.filter = filter;
    }

    /**
     * @return Responds to a GET request with the OpenAPI definition for the {@link MicroserviceRestApplication}.
     */
    @Override
    public MicroservletResponse onGet()
    {
        return new Response();
    }

    /**
     * @return An OpenAPI definition for the {@link JettyMicroservletFilter} passed to the constructor
     */
    public OpenAPI openApi()
    {
        var api = new OpenAPI();

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
    private void addInfo(OpenAPI api)
    {
        api.info(filter.restApplication().openApiInfo());
    }

    /**
     * Adds each path that is mounted on the {@link JettyMicroservletFilter}. The {@link PathItem} for each path is
     * resolved by {@link #pathItem(Microservlet)}.
     */
    private void addPaths(OpenAPI api)
    {
        var paths = new Paths();

        // Got through each mount path that the filter has,
        for (var path : filter.paths())
        {
            // and add a PathItem to the list of paths.
            paths.addPathItem(path.join(), pathItem(filter.microservlet(path)));
        }

        // Add the paths to the OpenAPI object
        api.paths(paths);
    }

    /**
     * Add {@link Schema}s for all the models in {@link #models} set. This set is populated during {@link
     * #addPaths(OpenAPI)}.
     */
    private void addSchemas(OpenAPI api)
    {
        var schemas = api.getComponents().getSchemas();
        for (var model : models)
        {
            schemas.putAll(ModelConverters.getInstance().read(model));
        }
    }

    private boolean methodAnnotationExists(
            final Class<? extends MicroservletRequest> requestType,
            final String method,
            final Class<? extends Annotation> annotationClass)
    {
        try
        {
            var annotation = requestType
                    .getMethod(method)
                    .getAnnotation(annotationClass);

            return annotation != null;
        }
        catch (NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return false;
        }
    }

    private <T extends Annotation> String methodAnnotationValue(
            final Class<? extends MicroservletRequest> requestType,
            final String method,
            final Class<T> annotationClass,
            final Function<T, String> function)
    {
        try
        {
            var annotation = requestType
                    .getMethod(method)
                    .getAnnotation(annotationClass);

            return annotation == null ? null : function.apply(annotation);
        }
        catch (NoSuchMethodException e)
        {
            problem(e, "No $() method in $", method, requestType);
            return null;
        }
    }

    /**
     * @return A new {@link ApiResponse} for the given description and schema.
     */
    private ApiResponse newResponse(String description, Schema<?> schema)
    {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("application/json", new MediaType().schema(schema)));
    }

    private Operation operationGet(final Class<? extends MicroservletGet> requestType,
                                   final Class<? extends MicroservletResponse> responseType)
    {
        return unsupported();
    }

    /**
     * @param requestType The type of the POSTed JSON request object
     * @param responseType The type of the JSON response object
     * @return The OpenAPI {@link Operation} describing this post operation
     */
    private Operation operationPost(final Class<? extends MicroservletPost> requestType,
                                    final Class<? extends MicroservletResponse> responseType)
    {
        // Create an instance of the request type,
        var request = Classes.newInstance(this, requestType);
        if (request != null)
        {
            var reader = new Reader();

            var context = new GenericOpenApiContext<>();
            var openApi = context.read();

            // then create an operation and populate it with the summary and description of the request,
            var operation = new Operation();
            operation.summary(methodAnnotationValue(requestType, "onPost", io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::summary));
            operation.description(methodAnnotationValue(requestType, "onPost", io.swagger.v3.oas.annotations.Operation.class, io.swagger.v3.oas.annotations.Operation::description));

            // next, create a parameter object and populate it from the request,
            var parameters = new ArrayList<Parameter>();
            if (methodAnnotationExists(requestType, "onPost", io.swagger.v3.oas.annotations.Parameter.class))
            {
                var parameter = new Parameter();
                parameter.name(methodAnnotationValue(requestType, "onPost", io.swagger.v3.oas.annotations.Parameter.class, io.swagger.v3.oas.annotations.Parameter::name));
                parameter.description(requestType.getSimpleName());
                parameter.setRequired(true);
                parameter.setExample(null);
                parameter.setContent(requestContent(requestType));
            }

            // add any parameters to the operation,
            operation.parameters(parameters);

            // add success and error responses,
            operation.responses(new ApiResponses()
                    .addApiResponse("200", responseSuccess(responseType))
                    .addApiResponse("403", newResponse("Forbidden", null))
                    .addApiResponse("404", newResponse("Not Found", null))
                    .addApiResponse("500", newResponse("Server Error", schemaError())));

            // and return the operation
            return operation;
        }

        return null;
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
        // Create the path item and give it the microservlet's description,
        var item = new PathItem();
        item.description(microservlet.description());

        // get the request and response types,
        var requestType = microservlet.requestType();
        var responseType = microservlet.responseType();

        // add them to the set of models,
        models.add(requestType);
        models.add(responseType);

        // and if it's a get request,
        if (requestType.isAssignableFrom(MicroservletGet.class))
        {
            // then add a get operation description,
            item.get(operationGet((Class<? extends MicroservletGet>) requestType, responseType));
        }

        // and if it's a post request,
        if (requestType.isAssignableFrom(MicroservletPost.class))
        {
            // add a post operation description.
            item.post(operationPost((Class<? extends MicroservletPost>) requestType, responseType));
        }

        return item;
    }

    /**
     * @return A {@link Content} object for the given request type
     */
    private Content requestContent(final Class<? extends MicroservletPost> requestType)
    {
        // Add the request type to the set of models,
        models.add(requestType);

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
        models.add(responseType);

        // and return a 200 response with the schema for the response type.
        return newResponse("200", schema(responseType));
    }

    /**
     * @return The {@link Schema} for the given model
     */
    @SuppressWarnings("rawtypes")
    private Schema<?> schema(Class<?> model)
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

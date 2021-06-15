package com.telenav.kivakit.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.telenav.kivakit.aws.s3.S3Service;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.Resource;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionCode;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static software.amazon.awssdk.services.lambda.model.InvocationType.REQUEST_RESPONSE;

/**
 * An AWS lambda function with a Request and Response object, which are converted to/from JSON by the GsonObjectFactory
 * instance provided by the {@link LambdaService} that created this function. This lambda function can be published to
 * AWS with {@link #publish(Resource)}, passing in the name of the shaded jar with the lambda function in it. This
 * lambda function can be invoked as a normal Java {@link Function} with {@link Function#apply(Object)}. To create a
 * {@link Lambda} object, use {@link LambdaService#lambda(String, Class, Class, Function)}, or subclass this class and
 * provide an implementation of {@link #onRun(Object)}.
 *
 * @param <Request> The request object type
 * @param <Response> The response object type
 * @author jonathanl (shibo)
 */
public class Lambda<Request, Response> implements Listener, RequestStreamHandler, Function<Request, Response>
{
    private final Logger logger = new ConsoleLogger();

    /** The service that created this lambda function */
    private final LambdaService service;

    /** The name of this lambda function */
    private final String name;

    /** The type of the request object */
    private final Class<Request> requestType;

    /** The type of the response object */
    private final Class<Response> responseType;

    /** The Gson JSON codec */
    private final Gson gson;

    /** The code to run */
    private final Function<Request, Response> lambda;

    /** The role to use when executing this lambda function */
    private String role;

    Lambda(final LambdaService service,
           final String name,
           final Class<Request> requestType,
           final Class<Response> responseType,
           final Function<Request, Response> lambda)
    {
        this.service = service;
        this.name = name;
        this.requestType = requestType;
        this.responseType = responseType;
        this.gson = service.gson();
        this.lambda = lambda;

        ConsoleLog.asynchronous(false);
    }

    protected Lambda(final Lambda<Request, Response> that)
    {
        this.service = that.service;
        this.name = that.name;
        this.requestType = that.requestType;
        this.responseType = that.responseType;
        this.gson = that.gson;
        this.lambda = that.lambda;
        this.role = that.role;
    }

    protected Lambda(final LambdaService service,
                     final Class<Request> requestType,
                     final Class<Response> responseType)
    {
        this.service = service;
        this.gson = service.gson();
        this.lambda = null;
        this.requestType = requestType;
        this.responseType = responseType;
        this.name = CaseFormat.hyphenatedName(getClass());
    }

    @Override
    public Response apply(final Request request)
    {
        final var requestJson = gson.toJson(request);

        final InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(name)
                .invocationType(REQUEST_RESPONSE)
                .payload(SdkBytes.fromString(requestJson, UTF_8))
                .build();

        final var response = service.client().invoke(invokeRequest);
        if (response.sdkHttpResponse().isSuccessful())
        {
            final var json = response.payload().asUtf8String();
            return gson.fromJson(json, responseType);
        }
        warning("Unable to invoke lambda $", this);
        return null;
    }

    public Lambda<Request, Response> copy()
    {
        return new Lambda<>(this);
    }

    @Override
    public void handleRequest(final InputStream input,
                              final OutputStream output,
                              final Context context)
    {
        try
        {
            try (final InputStreamReader reader = new InputStreamReader(input, UTF_8))
            {
                final var request = gson.fromJson(reader, requestType);
                try
                {
                    final var response = onRun(request);
                    try
                    {
                        try (final PrintWriter writer = new PrintWriter(output))
                        {
                            writer.print(gson.toJson(response));
                        }
                    }
                    catch (final Exception e)
                    {
                        warning(e, "Unable to write JSON response");
                    }
                }
                catch (final Exception e)
                {
                    warning(e, "Lambda execution failed");
                }
            }
        }
        catch (final Exception e)
        {
            problem(e, "Unable to read lambda request object");
        }
    }

    @Override
    public void onMessage(final Message message)
    {
        logger.log(message);
    }

    public String publish(final Resource resource)
    {
        final var key = name + ".lambda.zip";

        final var bucket = S3Service.create()
                .withRegion(service.region())
                .bucket(service.codeBucket());

        bucket.object(key).copyFrom(resource);

        final var code = FunctionCode.builder()
                .s3Bucket(service.codeBucket())
                .s3Key(key)
                .imageUri(service.image().toString())
                .build();

        final var request = CreateFunctionRequest.builder()
                .functionName(name)
                .code(code)
                .role(role)
                .publish(true)
                .codeSigningConfigArn(service.codeSigningArn())
                .build();

        final var response = service.client().createFunction(request);
        if (response.sdkHttpResponse().isSuccessful())
        {
            return response.version();
        }
        return null;
    }

    @Override
    public String toString()
    {
        return Message.format("${class} $(${class})", name, requestType, responseType);
    }

    public Lambda<Request, Response> withRole(final String role)
    {
        final var copy = copy();
        copy.role = role;
        return copy;
    }

    protected Response onRun(final Request request)
    {
        return lambda.apply(request);
    }
}

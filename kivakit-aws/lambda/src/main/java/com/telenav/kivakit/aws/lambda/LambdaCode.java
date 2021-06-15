package com.telenav.kivakit.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.telenav.kivakit.aws.s3.S3Service;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.Resource;
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionCode;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The code that implements an AWS Lambda function.
 * <p>
 * This lambda code can be published to AWS with {@link #publish(Resource)}, passing in the shaded jar resource with the
 * lambda function in it. The {@link #handleRequest(InputStream, OutputStream, Context)} method is implemented by
 * calling {@link #onRun(Object)}, which executes the subclass implementation or the code provided by {@link
 * #withCode(Function)}.
 *
 * @author jonathanl (shibo)
 */
public class LambdaCode<Request, Response> implements Listener, RequestStreamHandler
{
    private final Logger logger = new ConsoleLogger();

    /** The role to use when executing this lambda function */
    private String role;

    /** The code to run */
    private Function<Request, Response> code;

    /** The lambda function description */
    private final Lambda<Request, Response> lambda;

    /** URI to machine image to use when running this lambda code */
    private URI machineImage;

    /** S3 bucket where code is stored */
    private String codeBucket;

    /** ARN to code signing resource */
    private String codeSigningArn;

    /** The lambda service for this code */
    private final LambdaService service;

    protected LambdaCode(final Lambda<Request, Response> lambda)
    {
        this.lambda = lambda;
        this.service = lambda.service();
    }

    protected LambdaCode(final LambdaCode<Request, Response> that)
    {
        this.code = that.code;
        this.role = that.role;
        this.lambda = that.lambda;
        this.service = that.service;
        this.codeSigningArn = that.codeSigningArn;
        this.machineImage = that.machineImage;
        this.codeBucket = that.codeBucket;
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
                final var request = lambda.toRequest(IO.string(reader));
                try
                {
                    final var response = onRun(request);
                    try
                    {
                        try (final PrintWriter writer = new PrintWriter(output))
                        {
                            writer.print(lambda.toResponseJson(response));
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

    /**
     * Publishes this lambda code to AWS from the given resource zip file
     *
     * @param resource The resource to publish
     * @return The version of the lambda function or null if it was not published
     */
    public String publish(final Resource resource)
    {
        final var key = lambda.name() + ".lambda.zip";

        final var bucket = S3Service.create()
                .withRegion(service.region())
                .bucket(codeBucket);

        bucket.object(key).copyFrom(resource);

        final var code = FunctionCode.builder()
                .s3Bucket(codeBucket)
                .s3Key(key)
                .imageUri(machineImage.toString())
                .build();

        final var request = CreateFunctionRequest.builder()
                .functionName(lambda.name())
                .code(code)
                .role(role)
                .publish(true)
                .codeSigningConfigArn(codeSigningArn)
                .build();

        final var response = service.client().createFunction(request);
        if (response.sdkHttpResponse().isSuccessful())
        {
            return response.version();
        }
        return null;
    }

    public LambdaCode<Request, Response> withCode(final Function<Request, Response> code)
    {
        final var copy = copy();
        copy.code = code;
        return copy;
    }

    public LambdaCode<Request, Response> withCodeBucket(final String bucket)
    {
        final var copy = copy();
        copy.codeBucket = bucket;
        return copy;
    }

    public LambdaCode<Request, Response> withCodeSigningArn(final String codeSigningArn)
    {
        final var copy = copy();
        copy.codeSigningArn = codeSigningArn;
        return copy;
    }

    public LambdaCode<Request, Response> withMachineImage(final URI image)
    {
        final var copy = copy();
        copy.machineImage = image;
        return copy;
    }

    public LambdaCode<Request, Response> withRole(final String role)
    {
        final var copy = copy();
        copy.role = role;
        return copy;
    }

    protected LambdaCode<Request, Response> copy()
    {
        return new LambdaCode<>(this);
    }

    protected Response onRun(final Request request)
    {
        return code.apply(request);
    }
}

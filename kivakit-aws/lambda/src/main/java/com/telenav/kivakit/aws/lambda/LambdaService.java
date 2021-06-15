package com.telenav.kivakit.aws.lambda;

import com.google.gson.Gson;
import com.telenav.kivakit.aws.core.AwsRegion;
import com.telenav.kivakit.aws.core.AwsService;
import com.telenav.kivakit.configuration.lookup.Lookup;
import com.telenav.kivakit.serialization.json.GsonFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.net.URI;
import java.util.function.Function;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * A wrapper around AWS Lambda that makes it easy to publish and invoke simple lambda functions.
 *
 * <p><b>Example</b></p>
 * <pre>
 * public class MyBaseLambdaFunction extends Lambda
 * {
 *     protected LambdaService service()
 *     {
 *         return LambdaService.create()
 *             .withMachineImage(imageUri)
 *             .withCodeBucket("my-lambda-bucket")
 *             .withCodeSigningArn(arn)
 *             .withGsonFactory(new MyApplicationGsonFactory());
 *     }
 * }
 *
 * public class MyLambdaFunction extends MyBaseLambdaFunction
 * {
 *     public MyLambdaFunction()
 *     {
 *         super(service(), MyRequest.class, MyResponse.class);
 *     }
 *
 *     public MyResponse onRun(MyRequest request)
 *     {
 *         return [ ... ]
 *     }
 *
 *     public static void main(String[] arguments)
 *     {
 *         // Publish lambda function when invoked from build script
 *         new MyLambdaFunction().publish(new File(arguments[0]));
 *     }
 * }
 *
 * // Apply lambda function
 * MyRequest request = [ ... ];
 * var result = new MyLambdaFunction()
 *     .withRole("my-role")
 *     .apply(request);
 * </pre>
 *
 * @author jonathanl (shibo)
 */
public class LambdaService extends AwsService
{
    public static LambdaService create()
    {
        return new LambdaService();
    }

    private GsonFactory gsonFactory;

    private LambdaClient client;

    private URI machineImage;

    private String codeBucket;

    private String codeSigningArn;

    protected LambdaService()
    {
        ensureNotNull(this.gsonFactory = Lookup.global().locate(GsonFactory.class));
    }

    protected LambdaService(final LambdaService that)
    {
        super(that);
        this.codeSigningArn = that.codeSigningArn;
        this.machineImage = that.machineImage;
        this.codeBucket = that.codeBucket;
        this.gsonFactory = that.gsonFactory;
        this.client = that.client;
    }

    public LambdaClient client()
    {
        if (client == null)
        {
            final var client = LambdaClient.builder()
                    .region(region().region())
                    .build();
        }
        return client;
    }

    public String codeBucket()
    {
        return codeBucket;
    }

    public String codeSigningArn()
    {
        return codeSigningArn;
    }

    @Override
    public LambdaService copy()
    {
        return new LambdaService(this);
    }

    public Gson gson()
    {
        return gsonFactory.newInstance();
    }

    public URI image()
    {
        return machineImage;
    }

    /**
     * Creates a {@link Lambda} that handles AWS Lambda requests by executing the given function
     *
     * @param requestType The type of the request object
     * @param lambda The lambda code body
     * @return The lambda function
     */
    public <Request, Response> Lambda<Request, Response> lambda(final String name,
                                                                final Class<Request> requestType,
                                                                final Class<Response> responseType,
                                                                final Function<Request, Response> lambda)
    {
        return new Lambda<>(this, name, requestType, responseType, lambda);
    }

    public LambdaService withCodeBucket(final String bucket)
    {
        final var copy = copy();
        copy.codeBucket = bucket;
        return copy;
    }

    public LambdaService withCodeSigningArn(final String codeSigningArn)
    {
        final var copy = copy();
        copy.codeSigningArn = codeSigningArn;
        return copy;
    }

    @Override
    public LambdaService withCredentialsProvider(final AwsCredentialsProvider region)
    {
        return (LambdaService) super.withCredentialsProvider(region);
    }

    public LambdaService withGsonFactory(final GsonFactory factory)
    {
        final var copy = copy();
        copy.gsonFactory = factory;
        return copy;
    }

    public LambdaService withMachineImage(final URI image)
    {
        final var copy = copy();
        copy.machineImage = image;
        return copy;
    }

    @Override
    public LambdaService withRegion(final AwsRegion region)
    {
        return (LambdaService) super.withRegion(region);
    }
}

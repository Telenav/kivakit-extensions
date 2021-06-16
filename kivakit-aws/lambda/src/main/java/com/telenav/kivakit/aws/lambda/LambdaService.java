package com.telenav.kivakit.aws.lambda;

import com.google.gson.Gson;
import com.telenav.kivakit.aws.core.AwsRegion;
import com.telenav.kivakit.aws.core.AwsService;
import com.telenav.kivakit.configuration.lookup.Lookup;
import com.telenav.kivakit.serialization.json.GsonFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.lambda.LambdaClient;

/**
 * A wrapper around the AWS Lambda service, providing a configured {@link LambdaClient} and a {@link GsonFactory} to
 * {@link Lambda}.
 *
 * @author jonathanl (shibo)
 */
public class LambdaService extends AwsService
{
    public static LambdaService create()
    {
        return new LambdaService();
    }

    /** Factory for configured {@link Gson} objects */
    private GsonFactory gsonFactory;

    /** Configured lambda client for this lambda service */
    private LambdaClient client;

    protected LambdaService()
    {
        super("lambda");
    }

    protected LambdaService(final LambdaService that)
    {
        super(that);
        this.gsonFactory = that.gsonFactory;
        this.client = that.client;
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

    @Override
    public LambdaService withRegion(final AwsRegion region)
    {
        return (LambdaService) super.withRegion(region);
    }

    @Override
    protected LambdaService copy()
    {
        return new LambdaService(this);
    }

    LambdaClient client()
    {
        if (client == null)
        {
            final var client = LambdaClient.builder()
                    .region(region().region())
                    .build();
        }
        return client;
    }

    Gson gson()
    {
        if (this.gsonFactory == null)
        {
            this.gsonFactory = Lookup.global().locate(GsonFactory.class);
        }
        return gsonFactory.newInstance();
    }
}

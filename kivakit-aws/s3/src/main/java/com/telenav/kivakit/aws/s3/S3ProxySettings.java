package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsRegion;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class S3ProxySettings
{
    private AwsCredentialsProvider credentialsProvider;

    private AwsRegion region;

    public AwsCredentialsProvider credentialsProvider()
    {
        return credentialsProvider;
    }

    public S3ProxySettings credentialsProvider(final AwsCredentialsProvider credentialsProvider)
    {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public S3ProxySettings region(final AwsRegion region)
    {
        this.region = region;
        return this;
    }

    public AwsRegion region()
    {
        return region;
    }
}

package com.telenav.kivakit.aws.core;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

public abstract class AwsService
{
    private AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.builder().build();

    private AwsRegion region;

    protected AwsService()
    {
    }

    protected AwsService(final AwsService that)
    {
        this.credentialsProvider = that.credentialsProvider;
        this.region = that.region;
    }

    public AwsCredentialsProvider credentialsProvider()
    {
        return credentialsProvider;
    }

    public AwsRegion region()
    {
        return region;
    }

    public AwsService withCredentialsProvider(final AwsCredentialsProvider region)
    {
        final var copy = copy();
        copy.credentialsProvider = credentialsProvider;
        return copy;
    }

    public AwsService withRegion(final AwsRegion region)
    {
        final var copy = copy();
        copy.region = region;
        return copy;
    }

    protected abstract AwsService copy();
}

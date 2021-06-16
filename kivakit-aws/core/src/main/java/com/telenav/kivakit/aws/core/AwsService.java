package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.resource.ResourcePath;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.ServiceMetadata;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AwsService implements Named
{
    private AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.builder().build();

    private final String name;

    private AwsRegion region;

    private AwsAccount account;

    protected AwsService(final String name)
    {
        this.name = name;
    }

    protected AwsService(final AwsService that)
    {
        this.name = that.name;
        this.credentialsProvider = that.credentialsProvider;
        this.region = that.region;
        this.account = that.account;
    }

    public Arn arn(final String path)
    {
        return new Arn(name(), region, account, ResourcePath.parseUnixResourcePath(path));
    }

    public AwsCredentialsProvider credentialsProvider()
    {
        return credentialsProvider;
    }

    @Override
    public String name()
    {
        return name;
    }

    public AwsRegion region()
    {
        return region;
    }

    /**
     * @return The list of regions where this service is supported
     */
    public List<AwsRegion> regions()
    {
        return ServiceMetadata.of(name)
                .regions()
                .stream()
                .map(AwsRegion::new)
                .collect(Collectors.toList());
    }

    public AwsService withAccount(final AwsAccount account)
    {
        final var copy = copy();
        copy.account = account;
        return copy;
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

package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsRegion;
import com.telenav.kivakit.aws.core.AwsService;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

/**
 * AWS Simple Storage Service (S3) service.
 */
public class S3Service extends AwsService
{
    /**
     * @return Create {@link S3Service} object
     */
    public static S3Service create()
    {
        return new S3Service();
    }

    protected S3Service()
    {
    }

    protected S3Service(final S3Service that)
    {
        super(that);
    }

    /**
     * @return The bucket at the given path
     */
    public Bucket bucket(final String path)
    {
        return Bucket.bucketForPath(this, path);
    }

    @Override
    public S3Service copy()
    {
        return new S3Service(this);
    }

    @Override
    public S3Service withCredentialsProvider(final AwsCredentialsProvider region)
    {
        return (S3Service) super.withCredentialsProvider(region);
    }

    @Override
    public S3Service withRegion(final AwsRegion region)
    {
        return (S3Service) super.withRegion(region);
    }
}

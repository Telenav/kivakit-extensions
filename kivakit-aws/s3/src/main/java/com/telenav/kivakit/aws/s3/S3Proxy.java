package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsRegion;
import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.aws.core.security.AwsSecurityPolicy;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.trait.Trait;
import com.telenav.kivakit.resource.Resource;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

class S3Proxy implements Trait
{
    private S3Client client;

    public S3AccessControlList acl(final S3Object object)
    {
        return null;
    }

    public boolean acl(final S3Object object, final S3AccessControlList acl)
    {
        return false;
    }

    public ObjectList<Bucket> buckets(final Bucket bucket, final Matcher<Bucket> matcher)
    {
        return null;
    }

    public boolean copy(final BucketObject object, final Bucket destination)
    {
        return false;
    }

    public boolean copy(final Resource copyFrom, final BucketObject object)
    {
        return false;
    }

    public boolean create(final Bucket bucket)
    {
        return false;
    }

    public boolean delete(final S3Object object)
    {
        return false;
    }

    public boolean deleteTags(final S3Object object)
    {
        return false;
    }

    public boolean isWritable()
    {
        return false;
    }

    public ObjectList<BucketObject> objects(final Matcher<BucketObject> matcher)
    {
        return null;
    }

    public S3Proxy proxy()
    {
        return trait(S3Proxy.class, S3Proxy::new);
    }

    public AwsRegion region(final S3Object object)
    {
        return null;
    }

    public AwsSecurityPolicy securityPolicy(final S3Object object)
    {
        return null;
    }

    public boolean securityPolicy(final S3Object object, final AwsSecurityPolicy policy)
    {
        return false;
    }

    public AwsSecurityPolicy securityPolicy()
    {
        return null;
    }

    public boolean securityPolicy(final AwsSecurityPolicy policy)
    {
        return false;
    }

    public boolean tags(final S3Object object, final AwsTags tags)
    {
        return false;
    }

    public AwsTags tags(final S3Object object)
    {
        return null;
    }

    private S3Client client()
    {
        if (client == null)
        {
            final var settings = Settings.require(S3ProxySettings.class);

            client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(settings.credentialsProvider().resolveCredentials()))
                    .region(settings.region().region())
                    .build();
        }
        return client;
    }
}

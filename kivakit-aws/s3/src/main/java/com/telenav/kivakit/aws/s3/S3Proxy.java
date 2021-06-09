package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.aws.core.security.AwsSecurityPolicy;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.trait.Trait;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketTaggingRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetBucketTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

class S3Proxy extends BaseRepeater implements Trait
{
    private S3Client client;

    S3AccessControlList acl(final S3Object object)
    {
        return null;
    }

    boolean acl(final S3Object object, final S3AccessControlList acl)
    {
        return false;
    }

    ObjectList<Bucket> buckets(final Bucket bucket, final Matcher<Bucket> matcher)
    {
        return null;
    }

    boolean copy(final BucketObject object, final Bucket destination)
    {
        final var request = CopyObjectRequest.builder()
                .copySource(object.path().asString())
                .destinationBucket(destination.name())
                .build();

        return client.copyObject(request).copyObjectResult().lastModified() != null;
    }

    boolean copy(final Resource from, final BucketObject to)
    {
        final var request = PutObjectRequest.builder()
                .bucket(to.path().parent().asString())
                .key(to.key().identifier())
                .build();

        try (final var in = from.openForReading())
        {
            final var body = RequestBody.fromInputStream(in, -1);
            final var response = client.putObject(request, body);
            final var successful = response.sdkHttpResponse().isSuccessful();
            if (!successful)
            {
                warning("Unable to copy $ to $", from, to);
            }
            return successful;
        }
        catch (final IOException e)
        {
            warning(e, "Unable to copy $ to $", from, to);
            return false;
        }
    }

    boolean create(final Bucket bucket)
    {
        final var request = CreateBucketRequest.builder().bucket(bucket.name()).build();
        return client.createBucket(request).sdkHttpResponse().isSuccessful();
    }

    boolean delete(final S3Object object)
    {
        if (object instanceof Bucket)
        {
            final var bucket = (Bucket) object;
            final var request = DeleteBucketRequest.builder().bucket(bucket.name()).build();
            return client.deleteBucket(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var bucketObject = (BucketObject) object;
            final var request = DeleteObjectRequest.builder().bucket(bucketObject.name()).build();
            return client.deleteObject(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    boolean deleteTags(final S3Object object)
    {
        if (object instanceof Bucket)
        {
            final var bucket = (Bucket) object;
            final var request = DeleteBucketTaggingRequest.builder().bucket(bucket.name()).build();
            return client.deleteBucketTagging(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var bucketObject = (BucketObject) object;
            final var request = DeleteObjectTaggingRequest.builder().bucket(bucketObject.name()).build();
            return client.deleteObjectTagging(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    boolean isWritable()
    {
        return false;
    }

    ObjectList<BucketObject> objects(final Matcher<BucketObject> matcher)
    {
        return null;
    }

    AwsSecurityPolicy securityPolicy(final S3Object object)
    {
        return null;
    }

    boolean securityPolicy(final S3Object object, final AwsSecurityPolicy policy)
    {
        return false;
    }

    AwsSecurityPolicy securityPolicy()
    {
        return null;
    }

    boolean securityPolicy(final AwsSecurityPolicy policy)
    {
        return false;
    }

    boolean tags(final S3Object object, final AwsTags tags)
    {
        if (object instanceof Bucket)
        {
            final var bucket = (Bucket) object;
            final var request = DeleteBucketTaggingRequest.builder().bucket(bucket.name()).build();
            return client.deleteBucketTagging(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var bucketObject = (BucketObject) object;
            final var request = DeleteObjectTaggingRequest.builder().bucket(bucketObject.name()).build();
            return client.deleteObjectTagging(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    AwsTags tags(final S3Object object)
    {
        if (object instanceof Bucket)
        {
            final var bucket = (Bucket) object;
            final var request = GetBucketTaggingRequest.builder().bucket(bucket.name()).build();
            final var response = client.getBucketTagging(request);
            if (response.sdkHttpResponse().isSuccessful())
            {
                final var tags = new AwsTags();
                for (final var tag : response.tagSet())
                {
                    tags.put(tag.key(), tag.value());
                }
                return tags;
            }
            return null;
        }
        if (object instanceof BucketObject)
        {
            final var bucket = (BucketObject) object;
            final var request = GetObjectTaggingRequest.builder().bucket(bucket.name()).build();
            final var response = client.getObjectTagging(request);
            if (response.sdkHttpResponse().isSuccessful())
            {
                final var tags = new AwsTags();
                for (final var tag : response.tagSet())
                {
                    tags.put(tag.key(), tag.value());
                }
                return tags;
            }
            return null;
        }
        return unsupported();
    }

    private S3Client client()
    {
        if (client == null)
        {
            final var settings = Settings.require(S3Service.class);

            client = S3Client.builder()
                    .credentialsProvider(settings.credentialsProvider())
                    .region(settings.region().region())
                    .build();
        }
        return client;
    }
}

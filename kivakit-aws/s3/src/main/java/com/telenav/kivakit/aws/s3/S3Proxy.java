package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.aws.core.AwsUser;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketTaggingRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetBucketAclRequest;
import software.amazon.awssdk.services.s3.model.GetBucketTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectAclRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutBucketAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * Proxy to S3 using S3Client
 */
class S3Proxy extends BaseRepeater implements Mixin
{
    /** The underlying S3Client */
    private S3Client client;

    /**
     * @return The Access control list for the given S3 object
     */
    AccessControlPolicy acl(final S3ObjectMixin object)
    {
        if (object instanceof Bucket)
        {
            final var request = GetBucketAclRequest.builder()
                    .bucket(object.name())
                    .build();

            final var response = client(object).getBucketAcl(request);
            if (response.sdkHttpResponse().isSuccessful())
            {
                return AccessControlPolicy.builder()
                        .owner(response.owner())
                        .grants(response.grants())
                        .build();
            }
            return null;
        }

        if (object instanceof BucketObject)
        {
            final var request = GetObjectAclRequest.builder()
                    .bucket(object.name())
                    .build();

            final var response = client(object).getObjectAcl(request);
            if (response.sdkHttpResponse().isSuccessful())
            {
                return AccessControlPolicy.builder()
                        .owner(response.owner())
                        .grants(response.grants())
                        .build();
            }
            return null;
        }

        return unsupported();
    }

    /**
     * Sets the access control list for the given object
     *
     * @return True if the access control list was changed
     */
    boolean acl(final S3ObjectMixin object, final AccessControlPolicy acl)
    {
        if (object instanceof Bucket)
        {
            final var request = PutBucketAclRequest.builder()
                    .bucket(object.name())
                    .accessControlPolicy(acl)
                    .build();

            final var response = client(object).putBucketAcl(request);
            return response.sdkHttpResponse().isSuccessful();
        }

        if (object instanceof BucketObject)
        {
            final var request = PutObjectAclRequest.builder()
                    .bucket(object.name())
                    .accessControlPolicy(acl)
                    .build();

            final var response = client(object).putObjectAcl(request);
            return response.sdkHttpResponse().isSuccessful();
        }

        return unsupported();
    }

    /**
     * @return The list of buckets in the given bucket that match the given matcher
     */
    ObjectList<Bucket> buckets(final Bucket bucket, final Matcher<Bucket> matcher)
    {
        final var request = ListBucketsRequest.builder().build();
        final var response = client(bucket).listBuckets(request);

        final ObjectList<Bucket> buckets = new ObjectList<>();
        for (final var at : response.buckets())
        {
            final var child = bucket.bucket(at.name())
                    .withCreated(Time.milliseconds(at.creationDate().toEpochMilli()));
            if (matcher.matches(child))
            {
                buckets.add(child);
            }
        }
        return buckets;
    }

    /**
     * Copies from the given resource to the given bucket object
     *
     * @return True if the copy operation succeeded
     */
    boolean copyFrom(final BucketObject to, final Resource from)
    {
        final var request = PutObjectRequest.builder()
                .bucket(to.path().parent().asString())
                .key(to.key().identifier())
                .build();

        try (final var in = from.openForReading())
        {
            final var body = RequestBody.fromInputStream(in, -1);
            final var response = client(to).putObject(request, body);
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

    /**
     * Copies the given object to the given bucket
     *
     * @return True if the copy operation succeeded
     */
    boolean copyTo(final BucketObject object, final Bucket destination)
    {
        final var request = CopyObjectRequest.builder()
                .copySource(object.path().asString())
                .destinationBucket(destination.name())
                .build();

        return client(object).copyObject(request).copyObjectResult().lastModified() != null;
    }

    /**
     * Creates the given bucket
     *
     * @return True if the bucket was created
     */
    boolean create(final Bucket bucket)
    {
        final var request = CreateBucketRequest.builder().bucket(bucket.name()).build();
        return client(bucket).createBucket(request).sdkHttpResponse().isSuccessful();
    }

    /**
     * Deletes the given S3 object (bucket or object)
     */
    boolean delete(final S3ObjectMixin object)
    {
        if (object instanceof Bucket)
        {
            final var request = DeleteBucketRequest.builder().bucket(object.name()).build();
            return client(object).deleteBucket(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var request = DeleteObjectRequest.builder().bucket(object.name()).build();
            return client(object).deleteObject(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    /**
     * Removes tagging from the given bucket or object
     *
     * @return True if the tags were removed
     */
    boolean deleteTags(final S3ObjectMixin object)
    {
        if (object instanceof Bucket)
        {
            final var request = DeleteBucketTaggingRequest.builder().bucket(object.name()).build();
            return client(object).deleteBucketTagging(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var request = DeleteObjectTaggingRequest.builder().bucket(object.name()).build();
            return client(object).deleteObjectTagging(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    /**
     * @return True if the given bucket or object is writable by the given user
     */
    boolean isWritable(final S3ObjectMixin object, final AwsUser user)
    {
        final var acl = acl(object);
        for (final var grant : acl.grants())
        {
            if (grant.grantee().id().equals(user.identifier()))
            {
                switch (grant.permission())
                {
                    case FULL_CONTROL:
                    case WRITE:
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * @return The objects in the given bucket matching the given matcher
     */
    ObjectList<BucketObject> objects(final Bucket bucket, final Matcher<BucketObject> matcher)
    {
        final var request = ListObjectsRequest.builder().build();
        final var response = client(bucket).listObjects(request);

        final ObjectList<BucketObject> objects = new ObjectList<>();
        for (final var at : response.contents())
        {
            final var child = bucket.object(at.key())
                    .withLastModified(Time.milliseconds(at.lastModified().toEpochMilli()))
                    .withLength(Bytes.bytes(at.size()));
            if (matcher.matches(child))
            {
                objects.add(child);
            }
        }

        return objects;
    }

    /**
     * @return An input stream from which the given object can be read
     */
    InputStream openForReading(final BucketObject object)
    {
        final var request = GetObjectRequest.builder()
                .bucket(object.parent().name())
                .key(object.key().identifier())
                .build();

        final var response = client(object).getObject(request);
        if (response.response().sdkHttpResponse().isSuccessful())
        {
            return response;
        }
        return null;
    }

    /**
     * @return An output stream to write to the given object
     */
    OutputStream openForWriting(final BucketObject object)
    {
        return new S3OutputStream(object);
    }

    /**
     * Sets the tags on the given S3 object
     *
     * @return True if the tags were applied
     */
    boolean tags(final S3ObjectMixin object, final AwsTags tags)
    {
        if (object instanceof Bucket)
        {
            final var request = DeleteBucketTaggingRequest.builder().bucket(object.name()).build();
            return client(object).deleteBucketTagging(request).sdkHttpResponse().isSuccessful();
        }
        if (object instanceof BucketObject)
        {
            final var request = DeleteObjectTaggingRequest.builder().bucket(object.name()).build();
            return client(object).deleteObjectTagging(request).sdkHttpResponse().isSuccessful();
        }
        return unsupported();
    }

    /**
     * @return The tags on the given S3 object
     */
    AwsTags tags(final S3ObjectMixin object)
    {
        if (object instanceof Bucket)
        {
            final var request = GetBucketTaggingRequest.builder().bucket(object.name()).build();
            final var response = client(object).getBucketTagging(request);
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
            final var request = GetObjectTaggingRequest.builder().bucket(object.name()).build();
            final var response = client(object).getObjectTagging(request);
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

    private S3Client client(final S3ObjectMixin object)
    {
        if (client == null)
        {
            final var settings = object.service();
            var builder = S3Client.builder();
            if (settings.credentialsProvider() != null)
            {
                builder = builder.credentialsProvider(settings.credentialsProvider());
            }
            if (settings.region() != null)
            {
                builder = builder.region(settings.region().region());
            }
            return builder.build();
        }
        return client;
    }
}

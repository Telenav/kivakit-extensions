package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.resource.path.ResourcePathed;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;

/**
 * A mixin that adds functionality common to S3 objects ({@link Bucket}s and {@link BucketObject}s). This is not a
 * normal base class because {@link BucketObject} must inherit from {@link BaseWritableResource}.
 */
public interface S3ObjectMixin extends Named, ResourcePathed, Mixin
{
    /**
     * @return Gets the access control list for this S3 object
     */
    default AccessControlPolicy acl()
    {
        return proxy().acl(this);
    }

    /**
     * @return Sets the access control list for this S3 object
     */
    default boolean acl(final AccessControlPolicy acl)
    {
        return proxy().acl(this, acl);
    }

    /**
     * @return Deletes this S3 object
     */
    default boolean delete()
    {
        return proxy().delete(this);
    }

    /**
     * @return Deletes the tags on this S3 object
     */
    default boolean deleteTags()
    {
        return proxy().deleteTags(this);
    }

    /**
     * @return The parent bucket of this S3 object
     */
    Bucket parent();

    /**
     * @return The S3Proxy object associated with this {@link Mixin}
     */
    @SuppressWarnings("ClassEscapesDefinedScope")
    default S3Proxy proxy()
    {
        return state(S3Proxy.class, S3Proxy::new);
    }

    /**
     * @return The {@link S3Service} for this S3 object
     */
    S3Service service();

    /**
     * @return Sets the tags for this S3 object
     */
    default boolean tags(final AwsTags tags)
    {
        return proxy().tags(this, tags);
    }

    /**
     * @return Gets the tags for this S3 object
     */
    default AwsTags tags()
    {
        return proxy().tags(this);
    }
}

package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.aws.core.AwsUser;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A readable and writable object in an S3 {@link Bucket}.
 *
 * <p><b>Inherited S3ObjectTrait Methods</b></p>
 * <ul>
 *     <li>{@link #name()} - The name of this object (same as the key)</li>
 *     <li>{@link #parent()} - The parent {@link Bucket} of this object</li>
 *     <li>{@link #path()} - The path of bucket names to this object</li>
 *     <li>{@link #acl()} - Gets the access control list for this object</li>
 *     <li>{@link #acl(AccessControlPolicy)}  - Sets the access control list for this object</li>
 *     <li>{@link #delete()} - Deletes this object</li>
 *     <li>{@link #deleteTags()} - Deletes the tags on this object</li>
 *     <li>{@link #tags()} - Gets the tags for this object</li>
 *     <li>{@link #tags(AwsTags)} - Sets the tags on this object</li>
 *     <li>{@link #service()} - The {@link S3Service} to which this bucket belongs</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 * <ul>
 *     <li>{@link #key()} - The key of this object (same as the name)</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 * <ul>
 *     <li>{@link #copyFrom(Resource)} - Copies to this object from the given resource</li>
 *     <li>{@link #copyTo(Bucket)} - Copies this object to the given bucket</li>
 *     <li>{@link #isWritable()} - True if the calling user can write to this object</li>
 *     <li>{@link #isWritable(AwsUser)} - True if the given user can write to this object</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class BucketObject extends BaseWritableResource implements S3ObjectTrait
{
    private final Bucket parent;

    private final BucketObjectKey key;

    private Time lastModified;

    private Bytes length;

    BucketObject(final Bucket parent, final BucketObjectKey key)
    {
        this.parent = parent;
        this.key = key;
    }

    protected BucketObject(final BucketObject that)
    {
        this.key = that.key;
        this.parent = that.parent;
        this.lastModified = that.lastModified;
        this.length = that.length;
    }

    public BucketObject copy()
    {
        return new BucketObject(this);
    }

    /**
     * Copies the given resource to this object
     */
    public boolean copyFrom(final Resource copyFrom)
    {
        return proxy().copyFrom(this, copyFrom);
    }

    /**
     * Copies this object to the given destination bucket
     *
     * @return True if the copy operation succeeded
     */
    public boolean copyTo(final Bucket destination)
    {
        return proxy().copyTo(this, destination);
    }

    /**
     * @return True if this bucket object is writable by the calling user
     */
    @Override
    public Boolean isWritable()
    {
        return null;
    }

    /**
     * @return True if this object is writable by the given user
     */
    public boolean isWritable(final AwsUser identifier)
    {
        return proxy().isWritable(this, identifier);
    }

    /**
     * @return The name/key of this object
     */
    public BucketObjectKey key()
    {
        return key;
    }

    @Override
    public Time lastModified()
    {
        return lastModified;
    }

    /**
     * @return The name/key of this object
     */
    @Override
    public String name()
    {
        return key.identifier();
    }

    @Override
    public InputStream onOpenForReading()
    {
        return proxy().openForReading(this);
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return proxy().openForWriting(this);
    }

    /**
     * @return The parent of this bucket
     */
    @Override
    public Bucket parent()
    {
        return parent;
    }

    /**
     * @return The path to this object
     */
    @Override
    public ResourcePath path()
    {
        return super.path().withChild(key.identifier());
    }

    @Override
    public S3Service service()
    {
        return parent().service();
    }

    @Override
    public Bytes sizeInBytes()
    {
        return length;
    }

    public BucketObject withLastModified(final Time time)
    {
        final var copy = copy();
        copy.lastModified = time;
        return copy;
    }

    public BucketObject withLength(final Bytes length)
    {
        final var copy = copy();
        copy.length = length;
        return copy;
    }
}

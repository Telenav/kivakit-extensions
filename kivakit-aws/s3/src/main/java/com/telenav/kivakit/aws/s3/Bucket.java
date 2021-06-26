package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.ResourcePath;
import software.amazon.awssdk.services.s3.model.AccessControlPolicy;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalArgument;

/**
 * Represents a "bucket" on AWS Simple Storage Service (S3). Buckets can be created by calling {@link
 * S3Service#bucket(String)}.
 *
 * <p><b>Inherited S3ObjectTrait Methods</b></p>
 * <ul>
 *     <li>{@link #name()} - The name of this object (same as the key)</li>
 *     <li>{@link #parent()} - The parent {@link Bucket} of this object</li>
 *     <li>{@link #path()} - The path to this bucket</li>
 *     <li>{@link #acl()} - Gets the access control list for this object</li>
 *     <li>{@link #acl(AccessControlPolicy)}  - Sets the access control list for this object</li>
 *     <li>{@link #delete()} - Deletes this object</li>
 *     <li>{@link #deleteTags()} - Deletes the tags on this object</li>
 *     <li>{@link #tags()} - Gets the tags for this object</li>
 *     <li>{@link #tags(AwsTags)} - Sets the tags on this object</li>
 *     <li>{@link #service()} - The {@link S3Service} to which this bucket belongs</li>
 * </ul>
 *
 * <p><b>Buckets</b></p>
 * <ul>
 *     <li>{@link #bucket(String)} - The child bucket with the given name</li>
 *     <li>{@link #buckets(Matcher)} - All sub-buckets matching the given matcher</li>
 *     <li>{@link #buckets()} - All sub-buckets</li>
 * </ul>
 *
 * <p><b>Objects</b></p>
 * <ul>
 *     <li>{@link #object(BucketObjectKey)} - The {@link BucketObject} for the given key</li>
 *     <li>{@link #object(String)} - The {@link BucketObject} for the given key</li>
 *     <li>{@link #objects(Matcher)} - The objects in this bucket matching the given matcher</li>
 *     <li>{@link #objects()} - All objects in this bucket</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 * <ul>
 *     <li>{@link #create()} - Creates this bucket on S3</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class Bucket extends BaseRepeater implements S3ObjectMixin
{
    static Bucket bucketForName(final S3Service service, final String name)
    {
        if (name.length() < 3)
        {
            illegalArgument("Bucket name '$' is too short", name);
        }
        if (name.length() > 63)
        {
            illegalArgument("Bucket name '$' is too long", name);
        }
        if (!name.matches("[a-z0-9.-]"))
        {
            illegalArgument("Bucket name '$' is invalid", name);
        }
        return new Bucket(service, name);
    }

    static Bucket bucketForPath(final S3Service service, final String path)
    {
        Bucket at = null;
        for (final var name : StringList.split(path, "/").reversed())
        {
            final var next = bucketForName(service, name);
            if (at != null)
            {
                at.parent = next;
            }
            at = next;
        }
        return at;
    }

    private final S3Service service;

    private final String name;

    private Bucket parent;

    private Time created;

    protected Bucket(final Bucket that)
    {
        this.service = that.service;
        this.name = that.name;
        this.parent = that.parent;
        this.created = that.created;
    }

    private Bucket(final S3Service service, final String name)
    {
        this.service = service;
        this.name = name;
    }

    /**
     * @return The child bucket with the given name
     */
    public Bucket bucket(final String name)
    {
        final var child = Bucket.bucketForName(service, name);
        child.parent = this;
        return child;
    }

    /**
     * @return A list of all buckets in this bucket
     */
    public ObjectList<Bucket> buckets()
    {
        return buckets(new All<>());
    }

    /**
     * @return A list of all buckets in this bucket matching the given matcher
     */
    public ObjectList<Bucket> buckets(final Matcher<Bucket> matcher)
    {
        return proxy().buckets(this, matcher);
    }

    public Bucket copy()
    {
        return new Bucket(this);
    }

    /**
     * Creates this bucket on S3
     *
     * @return True if the bucket was created
     */
    public boolean create()
    {
        return proxy().create(this);
    }

    public Time created()
    {
        return created;
    }

    /**
     * @return The name of this bucket
     */
    @Override
    public String name()
    {
        return name;
    }

    /**
     * @return The bucket object for the given key
     */
    public BucketObject object(final BucketObjectKey key)
    {
        return new BucketObject(this, key);
    }

    /**
     * @return The bucket object for the given key
     */
    public BucketObject object(final String key)
    {
        return object(BucketObjectKey.parse(key));
    }

    /**
     * @return A list of all objects in this bucket
     */
    public ObjectList<BucketObject> objects()
    {
        return objects(new All<>());
    }

    /**
     * @return A list of all objects in this bucket matching the given matcher
     */
    public ObjectList<BucketObject> objects(final Matcher<BucketObject> matcher)
    {
        return proxy().objects(this, matcher);
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
     * @return The path of bucket names to this bucket
     */
    @Override
    public ResourcePath path()
    {
        return parent == null ? ResourcePath.parseResourcePath(name) : parent.path().withChild(name);
    }

    /**
     * @return The {@link S3Service} to which this bucket belongs
     */
    @Override
    public S3Service service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public Bucket withCreated(final Time time)
    {
        final var copy = copy();
        copy.created = time;
        return copy;
    }
}

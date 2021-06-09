package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.trait.Trait;
import com.telenav.kivakit.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.ResourcePath;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalArgument;

public class Bucket extends BaseRepeater implements S3Object, Trait
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
        return new Bucket(name);
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

    private S3Service service;

    private final String name;

    private Bucket parent;

    private Bucket(final String name)
    {
        this.name = name;
    }

    public ObjectList<Bucket> buckets()
    {
        return buckets(new All<>());
    }

    public ObjectList<Bucket> buckets(final Matcher<Bucket> matcher)
    {
        return proxy().buckets(this, matcher);
    }

    public Bucket child(final String name)
    {
        final var child = Bucket.bucketForName(service, name);
        child.parent = this;
        return child;
    }

    public boolean create()
    {
        return proxy().create(this);
    }

    @Override
    public String name()
    {
        return name;
    }

    public BucketObject object(final BucketObjectKey key)
    {
        return new BucketObject(this, key);
    }

    public BucketObject object(final String key)
    {
        return object(BucketObjectKey.parse(key));
    }

    public ObjectList<BucketObject> objects()
    {
        return objects(new All<>());
    }

    public ObjectList<BucketObject> objects(final Matcher<BucketObject> matcher)
    {
        return proxy().objects(matcher);
    }

    @Override
    public ResourcePath path()
    {
        return parent == null ? ResourcePath.parseResourcePath(name) : parent.path().withChild(name);
    }

    public S3Service service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

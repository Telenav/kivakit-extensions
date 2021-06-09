package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.trait.Trait;
import com.telenav.kivakit.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;

public class Bucket extends BaseRepeater implements S3Object, Trait
{
    static Bucket parse(final String name)
    {
        return new Bucket(name);
    }

    private final String name;

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
    public String toString()
    {
        return name;
    }
}

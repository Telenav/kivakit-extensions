package com.telenav.kivakit.aws.s3;

public class BucketObjectKey
{
    public static BucketObjectKey parse(final String key)
    {
        return new BucketObjectKey(key);
    }

    private final String identifier;

    public BucketObjectKey(final String identifier)
    {
        this.identifier = identifier;
    }

    public String identifier()
    {
        return identifier;
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}

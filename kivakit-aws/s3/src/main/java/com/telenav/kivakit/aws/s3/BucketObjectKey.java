package com.telenav.kivakit.aws.s3;

import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A key to an object in a bucket. {@link #parse(String)} check a key string for validity and returns a {@link
 * BucketObjectKey} or null if the key is invalid.
 *
 * @author jonathanl (shibo)
 */
public class BucketObjectKey
{
    /**
     * @see <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/object-keys.html">S3 Safe and Other Valid Key
     * Characters</a>
     */
    private static final Pattern VALID = Pattern.compile("[A-Za-z0-9/!_.*')(&$@=;:+ ,?-]");

    public static BucketObjectKey parse(final String key)
    {
        ensure(key.length() < 1024);
        ensure(VALID.matcher(key).matches());
        return new BucketObjectKey(key);
    }

    private final String identifier;

    private BucketObjectKey(final String identifier)
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

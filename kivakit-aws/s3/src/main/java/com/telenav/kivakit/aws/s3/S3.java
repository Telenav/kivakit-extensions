package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.kernel.language.objects.Lazy;

public class S3
{
    private static final Lazy<S3> instance = Lazy.of(S3::new);

    public static S3 get()
    {
        return instance.get();
    }

    public Bucket bucket(final String name)
    {
        return Bucket.parse(name);
    }
}

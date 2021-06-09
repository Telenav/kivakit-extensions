package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsUserIdentifier;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;

import java.io.InputStream;
import java.io.OutputStream;

public class BucketObject extends BaseWritableResource implements S3Object
{
    private final Bucket parent;

    private final BucketObjectKey key;

    BucketObject(final Bucket parent, final BucketObjectKey key)
    {
        this.parent = parent;
        this.key = key;
    }

    public boolean copyTo(final Bucket destination)
    {
        return false;
    }

    @Override
    public boolean isWritable()
    {
        return true;
    }

    public boolean isWritable(final AwsUserIdentifier identifier)
    {
        return false;
    }

    public BucketObjectKey key()
    {
        return key;
    }

    @Override
    public String name()
    {
        return key.identifier();
    }

    @Override
    public InputStream onOpenForReading()
    {
        return null;
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return null;
    }

    public Bucket parent()
    {
        return parent;
    }

    @Override
    public ResourcePath path()
    {
        return super.path().withChild(key.identifier());
    }

    public void save(final Resource copyFrom)
    {

    }
}

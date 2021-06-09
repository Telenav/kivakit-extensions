package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.aws.core.AwsTags;
import com.telenav.kivakit.aws.core.security.AwsSecurityPolicy;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.trait.Trait;
import com.telenav.kivakit.resource.path.ResourcePathed;

public interface S3Object extends Named, Trait, ResourcePathed
{
    default S3AccessControlList acl()
    {
        return proxy().acl(this);
    }

    default boolean acl(final S3AccessControlList acl)
    {
        return proxy().acl(this, acl);
    }

    default boolean delete()
    {
        return proxy().delete(this);
    }

    default boolean deleteTags()
    {
        return proxy().deleteTags(this);
    }

    default S3Proxy proxy()
    {
        return trait(S3Proxy.class, S3Proxy::new);
    }

    default AwsSecurityPolicy securityPolicy()
    {
        return proxy().securityPolicy(this);
    }

    default boolean securityPolicy(final AwsSecurityPolicy policy)
    {
        return proxy().securityPolicy(this, policy);
    }

    default boolean tags(final AwsTags tags)
    {
        return proxy().tags(this, tags);
    }

    default AwsTags tags()
    {
        return proxy().tags(this);
    }
}

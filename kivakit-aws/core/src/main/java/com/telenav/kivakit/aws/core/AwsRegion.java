package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.language.values.name.Name;
import software.amazon.awssdk.regions.Region;

public class AwsRegion extends Name
{
    public static AwsRegion region(final String name)
    {
        return new AwsRegion(Region.of(name));
    }

    private final Region region;

    public AwsRegion(final Region region)
    {
        this.region = region;
    }

    public AwsGateway gateway()
    {
        return AwsGateway.gateway(this);
    }

    public Region region()
    {
        return region;
    }
}

package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import software.amazon.awssdk.regions.Region;

public class AwsRegion extends StringIdentifier
{
    public static AwsRegion from(final Region region)
    {
        return new AwsRegion(region);
    }

    public static AwsRegion parse(final String name)
    {
        return new AwsRegion(Region.of(name));
    }

    private final Region region;

    protected AwsRegion(final Region region)
    {
        this.region = region;
    }

    public AwsGateway gateway()
    {
        return AwsGateway.gateway(this);
    }

    @Override
    public String identifier()
    {
        return region.id();
    }

    public boolean isGlobal()
    {
        return region.isGlobalRegion();
    }

    public Region region()
    {
        return region;
    }

    public AwsAvailabilityZone zone(final char zone)
    {
        return new AwsAvailabilityZone(this, zone);
    }

    public AwsAvailabilityZone zone()
    {
        return new AwsAvailabilityZone(this, 'a');
    }
}

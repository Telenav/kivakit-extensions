package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;

public class AwsAvailabilityZone extends StringIdentifier
{
    private final AwsRegion region;

    private final char zone;

    AwsAvailabilityZone(final AwsRegion region, final char zone)
    {
        this.region = region;
        this.zone = zone;
    }

    @Override
    public String identifier()
    {
        return region.identifier() + zone;
    }
}

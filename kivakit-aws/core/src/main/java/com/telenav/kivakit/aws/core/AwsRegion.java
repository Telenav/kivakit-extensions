package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import software.amazon.awssdk.regions.PartitionMetadata;
import software.amazon.awssdk.regions.Region;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalArgument;

public class AwsRegion extends StringIdentifier
{
    public static AwsRegion from(final Region region)
    {
        return new AwsRegion(region);
    }

    public static AwsRegion parse(final String identifier)
    {
        for (final var region : Region.regions())
        {
            if (region.id().equals(identifier))
            {
                return new AwsRegion(region);
            }
        }
        return illegalArgument("$ is not a valid region", identifier);
    }

    private final Region region;

    protected AwsRegion(final Region region)
    {
        this.region = region;
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

    public AwsPartition partition()
    {
        final var metadata = PartitionMetadata.of(region);
        return AwsPartition.parse(metadata.id());
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

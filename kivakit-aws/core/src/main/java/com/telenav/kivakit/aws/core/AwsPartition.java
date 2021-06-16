package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.interfaces.naming.Named;

public class AwsPartition implements Named
{
    public static AwsPartition parse(final String partition)
    {
        switch (partition)
        {
            case "aws":
            case "aws-cn":
            case "aws-us-gov":
                return new AwsPartition(partition);

            default:
                return Ensure.illegalArgument("Invalid partition: $", partition);
        }
    }

    private final String name;

    public AwsPartition(final String name)
    {
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }
}

package com.telenav.kivakit.aws.core;

public class AwsUser
{
    private final String identifier;

    public AwsUser(final String identifier)
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

package com.telenav.kivakit.aws.core;

/**
 * An AWS user with an identifier.
 *
 * @author jonathanl (shibo)
 */
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

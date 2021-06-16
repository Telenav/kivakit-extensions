package com.telenav.kivakit.aws.core;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalArgument;

/**
 * An AWS account number, without the dashes.
 *
 * @author jonathanl (shibo)
 */
public class AwsAccount
{
    public static AwsAccount parse(final String account)
    {
        if (account.matches("[0-9-]+"))
        {
            return new AwsAccount(account.replaceAll("-", ""));
        }
        return illegalArgument("Invalid AWS account number: $", account);
    }

    private final String identifier;

    public AwsAccount(final String identifier)
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

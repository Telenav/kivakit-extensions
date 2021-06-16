package com.telenav.kivakit.aws.core;

import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.ResourcePath;

import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalArgument;

/**
 * <p>
 * Amazon Resource Name (ARN) in the format:
 * </p>
 * <p>
 * &nbsp;&nbsp;&nbsp;&nbsp;arn:partition:service:region?:account?:resource-path
 * </p>
 * <p>
 * where resource-path matches the pattern: (resource-type[/:])*resource-id
 * </p>
 *
 * <p><b>ARN Components</b></p>
 * <ul>
 *    <li><i>partition</i> - The AWS region partition in which the resource is located: aws, aws-cn, aws-us-gov</li>
 *    <li><i>service</i> - The name of the AWS service, such as "s3" or "lambda"</li>
 *    <li><i>region</i> - The (optional) AWS region where the service is located, such as "us-west-1"</li>
 *    <li><i>account</i> - The (optional) AWS account that owns the resource, without hyphens</li>
 *    <li><i>resource-path</i> - The resource identifier, including any resource-type prefixes</li>
 * </ul>
 */
public class Arn
{
    private static final Pattern ARN_PATTERN = Pattern.compile("(?x)"
            + "arn"
            + ":(?<partition> aws|aws-cn|aws-us-gov)"
            + ":(?<service> [a-z]+)"
            + ":(?<region> [a-z0-9-]*)"
            + ":(?<account> [0-9]*)"
            + ":(?<resource> [A-Za-z/:]+)");

    public static Arn parse(final String name)
    {
        final var matcher = ARN_PATTERN.matcher(name);
        if (matcher.matches())
        {
            return new Arn
                    (
                            matcher.group("partition"),
                            matcher.group("service"),
                            matcher.group("region"),
                            matcher.group("account"),
                            matcher.group("resource")
                    );
        }
        return illegalArgument("Invalid ARN: $", name);
    }

    private final String service;

    private final AwsRegion region;

    private final AwsAccount account;

    private final ResourcePath path;

    Arn(final String service,
        final AwsRegion region,
        final AwsAccount account,
        final ResourcePath path)
    {
        this.region = region;
        this.service = service;
        this.account = account;
        this.path = path;
    }

    protected Arn(final String partition,
                  final String service,
                  final String region,
                  final String account,
                  final String path)
    {
        this.service = service;
        this.region = AwsRegion.parse(region);
        this.account = AwsAccount.parse(account);
        this.path = ResourcePath.parseUnixResourcePath(path);
    }

    public String endpoint()
    {
        return Message.format("https://$.amazonaws.com", service);
    }

    public AwsRegion region()
    {
        return region;
    }

    public String regionalEndpoint()
    {
        return Message.format("https://$.$.amazonaws.com", service, region);
    }

    public ResourcePath resourcePath()
    {
        return path;
    }

    public String service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return Message.format("arn:$:$:$:$:$", region.partition(), service, region, account, path);
    }
}

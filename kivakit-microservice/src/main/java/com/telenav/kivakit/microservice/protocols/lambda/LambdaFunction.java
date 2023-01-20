package com.telenav.kivakit.microservice.protocols.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.version.Version.parseVersion;
import static java.util.Objects.hash;

/**
 * <b>Not public API</b>
 *
 * <p>
 * An AWS Lambda function with a name and a version
 * </p>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
class LambdaFunction implements RegistryTrait
{
    /** The Lambda function name */
    private final String name;

    /** The Lambda function version */
    private final Version version;

    /**
     * Creates an AWS Lambda function
     *
     * @param name The name of the function
     * @param version The function version
     */
    public LambdaFunction(String name, Version version)
    {
        this.name = name;
        this.version = version;
    }

    /**
     * Creates an AWS Lambda function from a Lambda context
     *
     * @param context Information about a Lambda function
     */
    public LambdaFunction(Context context)
    {
        this.name = context.getFunctionName();
        this.version = parseVersion(context.getFunctionVersion());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LambdaFunction that)
        {
            return this.name.equals(that.name)
                    && version.equals(that.version);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hash(name, version);
    }

    @Override
    public String toString()
    {
        return name + "-" + version;
    }
}

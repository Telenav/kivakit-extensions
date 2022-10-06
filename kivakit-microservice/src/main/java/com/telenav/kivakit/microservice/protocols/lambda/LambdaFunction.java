package com.telenav.kivakit.microservice.protocols.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.annotations.code.CodeType;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.version.Version;

import java.util.Objects;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.version.Version.parseVersion;

/**
 * <b>Not public API</b>
 *
 * <p>
 * An AWS Lambda function with a name and a version
 * </p>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE,
             type = CodeType.CODE_PRIVATE)
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
        if (object instanceof LambdaFunction)
        {
            LambdaFunction that = (LambdaFunction) object;
            return this.name.equals(that.name)
                    && version.equals(that.version);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, version);
    }

    @Override
    public String toString()
    {
        return name + "-" + version;
    }
}

package com.telenav.kivakit.microservice.protocols.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequestHandler;
import com.telenav.kivakit.serialization.json.GsonFactory;

import java.util.Objects;

/**
 * <b>Not public API</b>
 *
 * <p>
 * An AWS Lambda function with a name and a version
 * </p>
 *
 * @author jonathanl (shibo)
 */
class LambdaFunction implements RegistryTrait
{
    private final String name;

    private final Version version;

    public LambdaFunction(String name, Version version)
    {
        this.name = name;
        this.version = version;
    }

    public LambdaFunction(Context context)
    {
        this.name = context.getFunctionName();
        this.version = Version.parse(Listener.throwing(), context.getFunctionVersion());
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

    public String toString()
    {
        return name + "-" + version;
    }
}

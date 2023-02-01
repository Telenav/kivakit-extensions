package com.telenav.kivakit.microservice.internal.yaml.reader;

import com.telenav.kivakit.microservice.internal.yaml.YamlBlock;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.microservice.internal.yaml.YamlBlock.block;
import static com.telenav.kivakit.microservice.internal.yaml.YamlLiteral.literal;
import static com.telenav.kivakit.microservice.internal.yaml.YamlScalar.scalar;

public class YamlReader
{
    public YamlBlock read(Resource resource)
    {
        return readBlock("", new YamlInput(resource));
    }

    private YamlBlock readBlock(String name, YamlInput in)
    {
        var result = block(name);
        for (var line : in.block())
        {
            switch (line.type())
            {
                case LABEL -> result = result.with(readBlock(line.label(), in));
                case SCALAR_STRING -> result = result.with(scalar(line.label(), line.string()));
                case SCALAR_NUMBER -> result = result.with(scalar(line.label(), line.number()));
                case LITERAL -> result = result.with(literal(line.label(), line.string()));
            }
        }
        return result;
    }
}

package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.data.formats.yaml.tree.YamlArray;
import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.tree.YamlNode;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.data.formats.yaml.tree.YamlArray.array;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlLiteral.literal;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlScalar.scalar;

/**
 * <p><b>Limitations</b></p>
 *
 * <ol>
 *     <li>Does not support maps</li>
 *     <li>Does not support nested arrays</li>
 *     <li>Does not support multiline strings</li>
 * </ol>
 *
 * @author Jonathan Locke
 */
public class YamlReader
{
    public YamlNode read(Resource resource)
    {
        var in = new YamlInput(resource);
        if (in.lookahead().isArrayElement())
        {
            return readArray("", in);
        }
        else
        {
            return readBlock("", in);
        }
    }

    private YamlArray readArray(String name, YamlInput in)
    {
        var result = array(name);
        YamlBlock element = null;
        for (var line : in.block())
        {
            if (line.isArrayElement())
            {
                if (element != null)
                {
                    result = result.with(element);
                }
                element = block("");
            }
            element = readNext(in, element, line);
        }
        if (element != null)
        {
            result = result.with(element);
        }
        return result;
    }

    private YamlBlock readBlock(String name, YamlInput in)
    {
        var result = block(name);
        for (var line : in.block())
        {
            result = readNext(in, result, line);
        }
        return result;
    }

    private YamlBlock readNext(YamlInput in, YamlBlock result, YamlLine line)
    {
        switch (line.type())
        {
            case LABEL -> result = result.with(readBlock(line.label(), in));
            case SCALAR_STRING -> result = result.with(scalar(line.label(), line.string()));
            case SCALAR_NUMBER -> result = result.with(scalar(line.label(), line.number()));
            case LITERAL -> result = result.with(literal(line.label(), line.string()));
        }
        return result;
    }
}

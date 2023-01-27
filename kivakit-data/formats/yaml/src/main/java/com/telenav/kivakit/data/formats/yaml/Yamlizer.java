package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlLiteral;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlScalar;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.string.Strings.doubleQuoted;

/**
 * Converts {@link YamlNode}s to text, in the form of a {@link StringList}.
 *
 * @author Jonathan Locke
 */
public class Yamlizer
{
    /**
     * Returns the yaml for the given node
     */
    public StringList asStringList(YamlNode node)
    {
        if (node instanceof YamlLiteral literal)
        {
            return asStringList(literal);
        }
        if (node instanceof YamlScalar scalar)
        {
            return asStringList(scalar);
        }
        if (node instanceof YamlBlock block)
        {
            return asStringList(block);
        }
        if (node instanceof YamlArray array)
        {
            return asStringList(array);
        }
        return fail("Unrecognized node type: $", node.getClass());
    }

    /**
     * Returns the yaml for the given literal
     */
    public StringList asStringList(YamlLiteral literal)
    {
        return stringList(literal.name() + ": " + literal.value());
    }

    /**
     * Returns the yaml for the given block
     */
    public StringList asStringList(YamlBlock block)
    {
        var yaml = stringList();

        // Add the yaml for each element of the block,
        for (var child : block.elements())
        {
            yaml.addAll(asStringList(child));
        }

        // then if the block is named,
        if (block.isNamed())
        {
            // indent the elements and add the label.
            yaml = yaml.indented(2)
                .prepending(block.name() + ":");
        }

        return yaml;
    }

    /**
     * Returns the yaml for the given scalar
     */
    public StringList asStringList(YamlScalar scalar)
    {
        if (scalar.isNumber())
        {
            return stringList(scalar.name() + ": " + scalar.number());
        }
        if (scalar.isBoolean())
        {
            return stringList(scalar.name() + ": " + scalar.truth());
        }
        if (scalar.isString())
        {
            return stringList(scalar.name() + ": " + doubleQuoted(scalar.string()));
        }

        return fail("Internal error");
    }

    /**
     * Returns the yaml for the given array
     */
    public StringList asStringList(YamlArray array)
    {
        var yaml = stringList();

        // Add the yaml for each element of the array,
        for (var element : array.elements())
        {
            var values = asStringList(element);
            yaml.addAll(arrayize(values));
        }

        // and if the array is named,
        if (array.isNamed())
        {
            // indent the elements and add the label.
            yaml = yaml.indented(2)
                .prepending(array.name() + ":");
        }

        return yaml;
    }

    /**
     * Returns the given yaml string list with the hyphen list indicator and element indentation added
     */
    private static StringList arrayize(StringList yaml)
    {
        var first = yaml.first();
        var rest = yaml.tail();
        return stringList("- " + first)
            .with(rest.indented(2));
    }
}

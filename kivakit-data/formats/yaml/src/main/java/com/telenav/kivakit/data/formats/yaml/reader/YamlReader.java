package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.data.formats.yaml.tree.YamlArray;
import com.telenav.kivakit.data.formats.yaml.tree.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.tree.YamlLiteral;
import com.telenav.kivakit.data.formats.yaml.tree.YamlNode;
import com.telenav.kivakit.data.formats.yaml.tree.YamlScalar;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlArray.array;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlLiteral.literal;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlScalar.scalar;

/**
 * A limited structured YAML reader, supporting blocks, literals, scalars and arrays.
 *
 * <p><b>Known Limitations</b></p>
 *
 * <ol>
 *     <li>Does not support maps</li>
 *     <li>Does not support multi-dimensional arrays</li>
 *     <li>Does not support multiline strings</li>
 * </ol>
 *
 * @author Jonathan Locke
 * @see YamlInput
 * @see YamlNode
 * @see YamlBlock
 * @see YamlArray
 * @see YamlLiteral
 * @see YamlScalar
 */
@SuppressWarnings("DuplicatedCode") public class YamlReader
{
    /**
     * Reads the given resource, returning the root node of a YAML tree.
     *
     * @param resource The resource to read
     * @return The YAML root node for the document in the resource
     */
    public YamlNode read(Resource resource)
    {
        // Create the YAML input to read the resource,
        var in = new YamlInput(resource);

        // and if the document is an array,
        if (in.lookahead().isArrayElement())
        {
            // then we read the array,
            return readArray(in);
        }

        // otherwise, the document is a block (labeled or not).
        return readBlock(in);
    }

    /**
     * Reads the next node from the input
     *
     * @param in The input
     * @return The next {@link YamlNode} in the input
     */
    private YamlNode read(YamlInput in)
    {
        // Make sure we have some input to read.
        ensure(in.hasMore());

        // If the current line type
        return switch (in.current().type())
            {
                // is a scalar, read that,
                case SCALAR_STRING, SCALAR_NUMBER -> readScalar(in);

                // and if it's a literal then read that,
                case LITERAL -> readLiteral(in);

                // otherwise it must be an array or block (either labeled or unlabeled).
                default ->
                {
                    // If the next line is an array element,
                    if (in.lookahead().isArrayElement())
                    {
                        // then read the input as an array,
                        yield readArray(in);
                    }
                    else
                    {
                        // otherwise read it as a block.
                        yield readBlock(in);
                    }
                }
            };
    }

    /**
     * Reads the next {@link YamlArray} from the input. The input must be pointing to the label for the array, or it
     * must be at the start of input (which would be an unlabeled array).
     *
     * @param in The input
     * @return The {@link YamlArray}
     */
    private YamlArray readArray(YamlInput in)
    {
        // Determine if the array is labeled,
        var labeled = in.current().isLabel();

        // ensure it has more input,
        ensure(in.hasMore());

        // and that the input is either labeled or we're at the document level (which can be an unlabeled array),
        ensure(labeled || in.isAtStartOfInput());

        // then create the array,
        var array = labeled
            ? array(in.read().label())
            : array();

        // and loop as long as we have input at the same indent level.
        var blockIndent = in.indentLevel();
        while (in.hasMore() && in.indentLevel() == blockIndent)
        {
            // If the next node is an array element,
            if (in.lookahead().isArrayElement())
            {
                // then we are looking at a lone scalar,
                array = array.with(readScalar(in));
            }
            else
            {
                // otherwise, we are looking at an (unlabeled) block element.
                array = array.with(readArrayElementBlock(in));
            }
        }

        return array;
    }

    /**
     * Reads an (unlabeled) array element {@link YamlBlock} from the input.
     *
     * @param in The input
     * @return The {@link YamlBlock}
     */
    private YamlBlock readArrayElementBlock(YamlInput in)
    {
        // Make sure that there's more input,
        ensure(in.hasMore());

        // and that there is no label,
        ensure(!in.current().isLabel());

        // and we're looking at an array element.
        ensure(in.current().isArrayElement());

        // Create the unlabeled block,
        var block = block();

        // then loop through elements at the same indent level,
        var blockIndent = in.indentLevel();
        while (in.hasMore() && in.indentLevel() == blockIndent)
        {
            // adding each element to the array element block.
            block = block.with(read(in));
        }

        return block;
    }

    /**
     * Reads the next {@link YamlBlock} from the input. The input must be pointing to the label for the block, or it
     * must be at the start of input (which is an unlabeled block).
     *
     * @param in The input
     * @return The {@link YamlBlock}
     */
    private YamlBlock readBlock(YamlInput in)
    {
        // Determine if we are looking at a labeled block,
        var labeled = in.current().isLabel();

        // ensure we have more input,
        ensure(in.hasMore());

        // and that we are either a labeled block or we are the document top-level block.
        ensure(labeled || in.isAtStartOfInput());

        // Create a block,
        var block = labeled
            ? block(in.current().label())
            : block();

        // and if it's labeled,
        if (labeled)
        {
            // skip the label,
            in.read();

            // and if the content is another label,
            if (in.current().isLabel())
            {
                // then simply read that block or array.
                return block.with(read(in));
            }
        }

        // While we have more input at the same indent level,
        var blockIndent = in.indentLevel();
        while (in.hasMore() && in.indentLevel() == blockIndent)
        {
            // add the next element to the block.
            block = block.with(read(in));
        }

        return block;
    }

    /**
     * Reads a {@link YamlLiteral} value from the input
     *
     * @param in The input
     * @return The {@link YamlLiteral}
     * @throws RuntimeException Thrown if the next line in the input is not a literal
     */
    private YamlLiteral readLiteral(YamlInput in)
    {
        // If we have more input,
        ensure(in.hasMore());

        // read that line,
        var next = in.read();

        // make sure it's a literal,
        ensure(next.type() == LITERAL);

        // and return it.
        return literal(next.label(), next.string());
    }

    /**
     * Reads a {@link YamlScalar} value from the input
     *
     * @param in The input
     * @return The {@link YamlScalar}
     * @throws RuntimeException Thrown if the next line in the input is not a scalar
     */
    private YamlScalar readScalar(YamlInput in)
    {
        // If we have more input,
        ensure(in.hasMore());

        // read it,
        var next = in.read();

        // then return it as
        return switch (next.type())
            {
                // a string scalar,
                case SCALAR_STRING -> scalar(next.label(), next.string());

                // or a numeric scalar.
                case SCALAR_NUMBER -> scalar(next.label(), next.number());

                default -> fail();
            };
    }
}

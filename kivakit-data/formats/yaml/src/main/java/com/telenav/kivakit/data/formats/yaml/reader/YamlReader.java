package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.data.formats.yaml.model.YamlLiteral;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlScalar;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.StringResource;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.yamlArray;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.yamlLiteral;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlEnumValue;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;

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
@SuppressWarnings("DuplicatedCode")
public class YamlReader
{
    /**
     * Reads a {@link YamlNode} from the given annotation on the given type
     *
     * @param type The type
     * @param annotationType The annotation
     * @param toText A callback to convert the annotation to text
     * @return The {@link YamlNode} from the annotated type
     */
    public static <T extends Annotation> YamlNode readYamlAnnotation(Class<?> type,
                                                                     Class<T> annotationType,
                                                                     Function<T, String> toText)
    {
        var annotation = ensureNotNull(type).getAnnotation(annotationType);
        if (annotation != null)
        {
            var text = toText.apply(annotation);
            return new YamlReader().read(new YamlInput(new StringResource(text)));
        }
        return null;
    }

    public static YamlReader yamlReader()
    {
        return new YamlReader();
    }

    protected YamlReader()
    {

    }

    /**
     * Reads the given text as YAML
     *
     * @param text The YAML text
     * @return The YAML
     */
    public YamlNode read(String text)
    {
        return read(new StringResource(text));
    }

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
                case STRING, NUMBER, ENUM_VALUE -> readScalar(in);

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

        // and that the input is either labeled or we're at the document level (which can be an unlabeled array).
        ensure(labeled || in.isAtStartOfInput());

        // Save the indent level of the block.
        var blockIndent = in.indentLevel();

        // Create an array,
        var array = labeled
            ? yamlArray(in.read().label())
            : yamlArray();

        // and if it's labeled,
        if (labeled)
        {
            // increase the block indent level, because the block is indented one.
            blockIndent++;
        }

        // and loop as long as we have input at the same indent level.
        while (in.hasMore() && in.indentLevel() == blockIndent)
        {
            // If the next node is an array element,
            if (in.lookahead() != null && in.lookahead().isArrayElement())
            {
                // then we are looking at a lone scalar,
                array = array.with(readScalar(in));
            }
            else
            {
                // otherwise, we are looking at an (unlabeled) block element.
                array = array.with(readArrayBlock(in));
            }
        }

        return array;
    }

    /**
     * Reads an array element {@link YamlBlock} from the input.
     *
     * @param in The input
     * @return The {@link YamlBlock}
     */
    private YamlBlock readArrayBlock(YamlInput in)
    {
        // Make sure that there's more input,
        ensure(in.hasMore());

        // and we're looking at an array element.
        ensure(in.current().isArrayElement());

        // Save the indent level of the block.
        var blockIndent = in.indentLevel();

        // Create the unlabeled block,
        var block = in.current().isLabel()
            ? yamlBlock(in.current().label())
            : yamlBlock();

        // then loop through elements at the same indent level or higher,
        while (in.hasMore() && in.indentLevel() == blockIndent)
        {
            // adding each element to the array element block.
            block = block.with(read(in));

            // If we are not at the end of input, and we are looking at the next array element,
            if (!in.hasMore() || in.current().isArrayElement())
            {
                // then return the finished block.
                return block;
            }
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

        // and that we are either at a labeled block or the document block.
        ensure(labeled || in.isAtStartOfInput());

        // Save the indent level of the block.
        var blockIndent = in.indentLevel();

        // Create a block,
        var block = labeled
            ? yamlBlock(in.read().label())
            : yamlBlock();

        // and if it's labeled,
        if (labeled)
        {
            // increase the block indent level, because the block is indented one.
            blockIndent++;
        }

        // While we have more input at the same indent level,
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
        return yamlLiteral(next.label(), next.string());
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

        try
        {
            // then return it as
            return switch (next.type())
                {
                    // a string scalar,
                    case STRING -> yamlScalar(next.label(), next.string());

                    // a numeric scalar,
                    case NUMBER -> yamlScalar(next.label(), next.number());

                    // or an enum value.
                    case ENUM_VALUE -> yamlEnumValue(next.string());

                    default -> fail();
                };
        }
        catch (Exception e)
        {
            throw new Problem(e, "Unable to parse scalar from:\n\n$\n\n$", next, in.resource()).asException();
        }
    }
}

package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.Stack;
import com.telenav.kivakit.core.language.trait.TryCatchTrait;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

public class YamlInput implements TryCatchTrait
{
    /** The list of lines */
    private final ObjectList<YamlLine> lines = new ObjectList<>();

    /** The index of the current line */
    private int at;

    /** The line most recently read */
    private YamlLine current;

    /** The last line read */
    private YamlLine previous;

    /** The next line that has not yet been read */
    private YamlLine lookahead;

    /** True if read() has not been called yet */
    private boolean atStartOfInput = true;

    /** The resourc being read */
    private final Resource resource;

    /**
     * Creates YAML input from the lines in the given resource
     *
     * @param resource The resource
     * @throws RuntimeException Thrown if block indentation is not valid
     */
    public YamlInput(Resource resource)
    {
        this.resource = resource;

        // Read all lines from the input resource,
        readLines(resource);

        // prime the lookahead value,
        advance();

        // and if there is more,
        if (lookahead != null)
        {
            // then read the current value.
            advance();
        }
    }

    /**
     * Returns the current index in the input
     */
    public int at()
    {
        return at;
    }

    /**
     * Returns the current line in the input
     */
    public YamlLine current()
    {
        return current;
    }

    /**
     * Returns true if there is a next line in the input
     */
    public boolean hasMore()
    {
        return current != null;
    }

    /**
     * Returns the array-adjusted indent level of the current line. Array adjustment involves reducing the indent level
     * for array elements after the first. For example, all these array elements are at level 0 even though the second
     * and third elements are indented by two spaces:
     *
     * <pre>
     * - first
     *   second
     *   third </pre>
     */
    public int indentLevel()
    {
        return current().indentLevel();
    }

    /**
     * Returns true if {@link #read()} has not yet been called.
     */
    public boolean isAtStartOfInput()
    {
        return atStartOfInput;
    }

    /**
     * Returns the next line in the input, without advancing the stream
     */
    public YamlLine lookahead()
    {
        return lookahead;
    }

    public YamlLine previous()
    {
        return previous;
    }

    /**
     * Reads the next line from input
     *
     * @return The next line
     */
    public YamlLine read()
    {
        // If we have a current line,
        if (current != null)
        {
            // save it,
            var read = current;

            // advance to the next line,
            advance();

            // make a note that we are not at start of input,
            atStartOfInput = false;

            // and return the original current line.
            return read;
        }

        return null;
    }

    public Resource resource()
    {
        return resource;
    }

    /**
     * Returns the number of lines (not including comments or blank lines)
     */
    public int size()
    {
        return lines.size();
    }

    /**
     * Advances to the next (non-comment, non-blank) line. The current line becomes the previous line, the current line
     * becomes the lookahead line, and the lookahead is set to the next line in the array of lines (if there is one).
     */
    private void advance()
    {
        // Keep the current value for posterity,
        previous = current;

        // advance the current line to the lookahead line,
        current = lookahead;

        // then read a new lookahead line,
        lookahead = at < size()
            ? lines.get(at++)
            : null;
    }

    /**
     * Reads all lines from the given resource into the lines field
     *
     * @param resource The resource to read
     */
    private void readLines(Resource resource)
    {
        // We are at line number 1.
        var lineNumber = 1;

        // We will track the previous line, so we can ensure that indentation never increases or
        // decreases by more than one level.
        YamlLine previous = null;

        // We will track the indentation of arrays because the spaces for array elements
        // increases the indentation one level beyond the true indentation level. For example,
        // element-1 below has indentation of 0, but so does element-2:
        //
        // - element-1
        //   element-2
        //
        var arrayIndentLevel = new Stack<Integer>();

        // Go through each line in the resource,
        var parser = new YamlLineParser();
        for (var line : resource.reader().readLines())
        {
            try
            {
                // Parse the line into a YamlLine model.
                var yaml = parser.parse(line)
                    .lineNumber(lineNumber++)
                    .ordinal(lines.size());

                // If we aren't looking at a comment or a blank line,
                if (!yaml.isComment() && !yaml.isBlank())
                {
                    // and the current line is an array element,
                    if (yaml.isArrayElement())
                    {
                        // and our indent level increased (we aren't at another list element at the same level),
                        if (yaml.rawIndentLevel() > previous.rawIndentLevel())
                        {
                            // then we push the current indent level,
                            arrayIndentLevel.push(yaml.rawIndentLevel());
                        }
                    }

                    // otherwise, we aren't at the start of an array, so we look
                    // at the top of the stack, and if we are below that indentation level,
                    var top = arrayIndentLevel.top();
                    if (top != null && yaml.rawIndentLevel() < top)
                    {
                        // we have left the array, so we pop the top indent level off of the stack.
                        arrayIndentLevel.pop();
                    }

                    // Unindent the current line by the number of levels of array nesting, but if it's
                    // the first element in a list, indent by one less.
                    var offset = yaml.isArrayElement() ? 1 : 0;
                    yaml.outdent(arrayIndentLevel.size() - offset);

                    // Add the current line.
                    lines.add(yaml);

                    // Make sure that the indent level has not increased by more than one level.
                    if (previous != null)
                    {
                        var delta = yaml.indentLevel() - previous.indentLevel();
                        ensure(delta <= 1, "Invalid indentation at line $", lines.size());
                    }
                    previous = yaml;
                }
            }
            catch (Exception e)
            {
                throw new Problem(e, "Unable to parse: $", resource).asException();
            }
        }
    }
}

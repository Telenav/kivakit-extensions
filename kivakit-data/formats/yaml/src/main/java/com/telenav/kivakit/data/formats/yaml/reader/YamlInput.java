package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.collections.iteration.BaseIterable;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.collection.NextIterator;
import com.telenav.kivakit.resource.Resource;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

public class YamlInput
{
    /** The list of lines */
    private final ObjectList<YamlLine> lines = new ObjectList<>();

    /** The index of the current line */
    private int at;

    /** The current line */
    private YamlLine current;

    /** The next line */
    private YamlLine lookahead;

    /**
     * Creates YAML input from the lines in the given resource
     *
     * @param resource The resource
     * @throws RuntimeException Thrown if block indentation is not valid
     */
    public YamlInput(Resource resource)
    {
        readLines(resource);

        next();
    }

    /**
     * Returns the current index
     */
    public int at()
    {
        return at;
    }

    /**
     * Returns the sequence of lines at the current indent level or greater
     *
     * @return The sequence of lines in the block
     */
    public Iterable<YamlLine> block()
    {
        if (current == null && hasNext())
        {
            next();
        }
        return new BaseIterable<>()
        {
            @Override
            protected NextIterator<YamlLine> newNextIterator()
            {
                int blockIndent = indentLevel();

                return () ->
                {
                    if (current != null && current.indentLevel() >= blockIndent)
                    {
                        var at = current();
                        next();
                        return at;
                    }
                    return null;
                };
            }
        };
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
    public boolean hasNext()
    {
        return lookahead != null;
    }

    /**
     * Returns the indent level of the current line
     */
    public int indentLevel()
    {
        return current().indentLevel();
    }

    /**
     * Returns the next (non-comment) line in the input, or null if there is none
     */
    public YamlLine next()
    {
        current = lookahead;

        lookahead = at < size()
            ? lines.get(at++)
            : null;

        return current;
    }

    /**
     * Returns the number of lines (including comments)
     */
    public int size()
    {
        return lines.size();
    }

    private void readLines(Resource resource)
    {
        var lineNumber = 1;
        YamlLine previous = null;

        for (var line : resource.reader().readLines())
        {
            var current = new YamlLine(line)
                .lineNumber(lineNumber++)
                .ordinal(lines.size());

            if (!current.isComment() && !current.isBlank())
            {
                lines.add(current);
            }

            ensure(previous == null || Math.abs(current.indentLevel() - previous.indentLevel()) <= 2,
                "Invalid indentation at line $", lines.size());

            previous = current;
        }
    }
}

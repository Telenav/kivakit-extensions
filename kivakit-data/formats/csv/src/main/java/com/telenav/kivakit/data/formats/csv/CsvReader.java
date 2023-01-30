////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.io.LookAheadReader;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.data.formats.csv.internal.lexakai.DiagramCsv;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.resource.Resource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;

/**
 * Parses a stream of CSV information. The rules outlined
 * <a href="https://en.wikipedia.org/wiki/Comma-separated_values">here</a> dictate valid CSV format.
 * In particular, this class can handle quoted strings, comments, empty lines, and other standard formatting
 * issues. However, it cannot handle line breaks within quotes.
 *
 * <p><b>Processing CSV Lines</b></p>
 *
 * <p>
 * A reader instance is constructed with {@link #CsvReader(Resource, CsvSchema, char, ProgressReporter)}. Since the
 * class is {@link Iterable}, lines can then be retrieved as objects with code similar to:
 *
 * <pre>
 * try (var reader = new CsvReader(resource, schema))
 * {
 *     for (var line : reader)
 *     {
 *         var employee = line.asObject(Employee.class);
 *
 *         [...]
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see CsvSchema
 * @see Resource
 * @see ProgressReporter
 * @see CsvLine
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@UmlRelation(label = "reads", referent = CsvLine.class)
@LexakaiJavadoc(complete = true)
public class CsvReader extends BaseIterator<CsvLine> implements RepeaterMixin, Closeable
{
    /** The separator for CSV (can be changed with setDelimiter) */
    protected char delimiter;

    /** Escape character */
    protected char escape = '\\';

    /** The input */
    protected final LookAheadReader in;

    /** The reporter for progress reading the CSV resource */
    private final ProgressReporter reporter;

    /** The CSV schema for lines to read */
    @UmlAggregation(label = "uses")
    private final CsvSchema schema;

    /** Quote character */
    private char quote = '"';

    public CsvReader(Resource resource,
                     CsvSchema schema,
                     char delimiter)
    {
        this(resource, schema, delimiter, nullProgressReporter());
    }

    public CsvReader(Resource resource,
                     CsvSchema schema)
    {
        this(resource, schema, ',');
    }

    /**
     * Constructs a reader for the given input stream and CSV schema
     */
    public CsvReader(Resource resource,
                     CsvSchema schema,
                     char delimiter,
                     ProgressReporter reporter)
    {
        this.reporter = reporter;
        this.schema = schema;
        this.delimiter = delimiter;
        var reader = resource.reader(nullProgressReporter()).textReader();
        if (reader == null)
        {
            throw new IllegalArgumentException("Unable to read: " + resource);
        }
        in = new LookAheadReader(reader);
    }

    /**
     * Closes the input
     */
    @Override
    public void close()
    {
        in.close();
    }

    /**
     * Sets the CSV delimiter (it's "," by default)
     */
    public void delimiter(char delimiter)
    {
        this.delimiter = delimiter;
    }

    /**
     * Returns the current line number in the input
     */
    public int lineNumber()
    {
        return in.lineNumber();
    }

    public Iterable<CsvLine> lines()
    {
        return () -> this;
    }

    public void problem()
    {
        reporter.problem();
    }

    public void problems(Count problems)
    {
        reporter.problems(problems);
    }

    public void problems(long problems)
    {
        reporter.problems(problems);
    }

    public CsvReader quote(char quote)
    {
        this.quote = quote;
        return this;
    }

    /**
     * Returns the schema for lines being read
     */
    public CsvSchema schema()
    {
        return schema;
    }

    /**
     * Skips the given number of lines
     */
    @SuppressWarnings("UnusedReturnValue")
    public CsvReader skipLines(int numberOfLinesToSkip)
    {
        for (var i = 0; i < numberOfLinesToSkip; i++)
        {
            next();
        }
        return this;
    }

    public CsvReader unquoted()
    {
        return quote((char) -1);
    }

    /**
     * Read the next CSV column from the input stream. When exiting this method the input stream should either be
     * pointing to a line end (0x0D), a column end (delimiter), or the end of the stream.
     *
     * @param in The input stream to read data from pointing to the start of the CSV column.
     * @return A buffer populated with the column data or null if no more data exists.
     */
    protected String extractNextColumn(LookAheadReader in)
    {
        var buffer = new StringBuffer();

        // Handle leading white spaces.
        processLeadingSpaces(in, buffer);

        // Determine what type of processing is needed.
        try
        {
            if (in.current() == quote)
            {
                readQuotedColumn(in, buffer);
            }
            else
            {
                readColumn(in, buffer);
            }
        }
        catch (Exception e)
        {
            problem(e, "Invalid CSV format");
            return null;
        }

        return in.hasNext() || buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CsvLine onNext()
    {
        if (in.hasNext())
        {
            var line = listenTo(new CsvLine(schema, delimiter));
            var lineNumber = in.lineNumber();
            line.lineNumber(lineNumber);
            if (lineNumber == 1)
            {
                reporter.start();
            }

            // Take care of comments.
            if (in.current() == '#' || (in.current() == '/' && in.lookAhead() == '/'))
            {
                skipLine(in);
                return findNext();
            }

            // Loop through pulling out the columns until an end of line or end
            // of file identifier is encountered.
            String column;
            while ((column = extractNextColumn(in)) != null)
            {
                line.add(column);

                if (in.atEndOfLine())
                {
                    // The end of the line has been reached, break out after
                    // advancing for the next entry.
                    in.next();
                    break;
                }
                if (in.current() == delimiter)
                {
                    in.next();
                    if (!in.hasNext())
                    {
                        // This is the case where there is a delimiter followed
                        // by the end of the file. So add an empty line and
                        // exit.
                        line.add("");
                        break;
                    }
                }
            }
            reporter.next();
            return line;
        }
        reporter.end();
        return null;
    }

    /**
     * Trims off the spaces at the beginning of a CSV column and copies them to the buffer.
     *
     * @param in The input stream to read from pointing to the start of the spaces.
     * @param buffer The buffer to be populated with the spaces.
     */
    protected void processLeadingSpaces(LookAheadReader in, StringBuffer buffer)
    {
        while (in.hasNext() && in.current() == ' ')
        {
            buffer.append(' ');
            in.next();
        }
    }

    /**
     * Reads a standard, non-quoted, CSV column. Note that when the method exits the stream should be pointing to the
     * line terminator, 0x0A, or be exhausted.
     *
     * @param in The input stream to read from pointing to the start of the column.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    protected void readColumn(LookAheadReader in, StringBuffer buffer)
    {
        while (in.hasNext() && !in.atEndOfLine())
        {
            if (in.current() == delimiter)
            {
                // Just catch this case and break out of the loop, it will be
                // handled later.
                break;
            }
            else if (in.current() == quote)
            {
                throw new IllegalArgumentException("Non quoted string contains quotes at line " + lineNumber());
            }
            else
            {
                if (in.current() == escape)
                {
                    in.next();
                }
                buffer.append((char) in.current());
            }
            in.next();
        }
    }

    /**
     * Reads a CSV column which begins with a quote. Note that when the method exits the stream should be pointing to
     * the line terminator, 0x0A, or be exhausted.
     *
     * @param in The input stream to read from pointing to the starting quote.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    protected void readQuotedColumn(LookAheadReader in, StringBuffer buffer)
    {
        // Read the character after the quote
        var second = (char) in.next();

        // If we're starting off with two double quotes ("")
        var terminator = quote;
        if (second == quote)
        {
            // and the next one is a quote too (""")
            if (in.lookAhead() == quote)
            {
                // read the quote,
                in.next();

                // then append the literal quote,
                buffer.append(quote);

                // and move to the next character,
                in.next();
            }
            else
            {
                // otherwise, if the next character is not a delimiter,
                // we have a column starting with something like ,""x
                if (in.lookAhead() != delimiter)
                {
                    // so append the quote,
                    buffer.append(quote);

                    // and move to the next character the 'x',
                    in.next();

                    // and we stop at the delimiter since we are not actually on a
                    // quoted column (we're on a column like ,""k"", which is unquoted)
                    terminator = delimiter;
                }
                else
                {
                    // otherwise, the column is empty, so we append nothing.
                    in.next();
                    return;
                }
            }
        }

        // White we haven't hit the closing quote or end of line,
        while (in.current() != terminator && !in.atEndOfLine())
        {
            if (in.current() == escape)
            {
                in.next();
            }

            // append the next character
            buffer.append((char) in.current());

            // and advance the input.
            in.next();

            // If we're looking at "",
            if (in.current() == quote && in.lookAhead() == quote)
            {
                // then it's a literal quote
                buffer.append(quote);
                in.next();
                in.next();
            }
        }

        if (in.atEndOfLine())
        {
            throw new IllegalArgumentException(
                "Quoted column never closed with matching quote at line " + lineNumber());
        }
        else
        {
            // Skip the close quote
            in.next();
        }
    }

    /**
     * Advances the input stream to the next line. This method can be used when comments, delimited by '#' are
     * encountered.
     *
     * @param in The input stream to be advanced.
     */
    protected void skipLine(LookAheadReader in)
    {
        while (in.hasNext())
        {
            if (in.atEndOfLine())
            {
                in.next();
                break;
            }
            in.next();
        }
    }
}

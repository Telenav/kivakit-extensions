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

import com.telenav.kivakit.core.io.LookAheadReader;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.data.formats.csv.internal.lexakai.DiagramCsv;
import com.telenav.kivakit.resource.Resource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Parses a stream of simplified CSV information. This class does not handle quoted entries, so the records cannot
 * contain the delimiter. The records also cannot embed a newline. This class is useful for handling CSVs that do not
 * use CSV quote rules for records. It is similar to and derived from class CsvReader except for the lack of double
 * quote handling.
 *
 * @author jonathanl (shibo)
 * @author ericg
 * @author roberts
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
public class UnquotedCsvReader extends CsvReader
{
    public UnquotedCsvReader(Resource resource,
                             CsvSchema schema,
                             char delimiter,
                             ProgressReporter reporter)
    {
        super(resource, schema, delimiter, reporter);
    }

    /**
     * Read the next CSV column from the input stream. When exiting this method the input stream should either be
     * pointing to a line end (0x0D), a column end (delimiter), or the end of the stream. This version ignores double
     * quotes, so the records cannot embed the delimiter.
     *
     * @param in The input stream to read data from pointing to the start of the CSV column.
     * @return A buffer populated with the column data or null if no more data exists.
     */
    @Override
    protected String extractNextColumn(LookAheadReader in)
    {
        var buffer = new StringBuffer();

        // Handle leading white spaces.
        processLeadingSpaces(in, buffer);

        // Determine what type of processing is needed.
        try
        {
            readColumn(in, buffer);
        }
        catch (Exception e)
        {
            problem(e, "Invalid CSV format");
            return null;
        }

        return in.hasNext() || buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * Reads a standard, non-quoted, CSV column. Note that when the method exits the stream should be pointing to the
     * line terminator, 0x0A, or be exhausted. This version ignores double quotes, so the records cannot embed the
     * delimiter.
     *
     * @param in The input stream to read from pointing to the start of the column.
     * @param buffer The buffer to be populated with the contents of the input data.
     */
    @Override
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
            else
            {
                buffer.append((char) in.current());
            }
            in.next();
        }
    }
}

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

package com.telenav.kivakit.logs.file;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.collections.map.string.VariableMap;
import com.telenav.kivakit.core.language.strings.StringTo;
import com.telenav.kivakit.language.time.Duration;
import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.logs.file.project.lexakai.DiagramLogsFile;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.OutputStream;

import static com.telenav.kivakit.ensure.Ensure.fail;

/**
 * A {@link Log} service provider that logs messages to text file(s). Configuration occurs via the command line. See
 * {@link LogServiceLogger} for details. Further details are available in the markdown help. The options available for
 * configuration with this logger are:
 *
 * <ul>
 *     <li><i>file</i> - The output file</li>
 *     <li><i>maximum-size</i> - The maximum size in {@link Bytes}</li>
 *     <li><i>rollover</i> -Rollover period (none, daily or hourly)</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsFile.class)
@LexakaiJavadoc(complete = true)
public class FileLog extends BaseRolloverTextLog
{
    private File file;

    private Duration maximumLogFileAge;

    @Override
    public void configure(VariableMap<String> properties)
    {
        super.configure(properties);

        var path = properties.get("file");
        if (path != null)
        {
            try
            {
                file = File.parse(Listener.console(), path);

                var rollover = properties.get("rollover");
                if (rollover != null)
                {
                    rollover(Rollover.valueOf(rollover.toUpperCase()));
                }

                maximumLogFileAge = properties.asObject("maximum-age",
                        new Duration.Converter(Listener.none()), Duration.MAXIMUM);

                maximumLogSize(properties.asObject("maximum-size",
                        new Bytes.Converter(Listener.none()), Bytes.MAXIMUM));
            }
            catch (Exception e)
            {
                fail(e, "FileLog file parameter '" + path + "' is not valid");
            }
        }
        else
        {
            fail("FileLog missing 'file' parameter");
        }
    }

    @Override
    public String name()
    {
        return "File";
    }

    @Override
    protected OutputStream newOutputStream()
    {
        return newFile().openForWriting();
    }

    private File newFile()
    {
        var newFile = File.parse(Listener.console(), file.withoutExtension() + "-" + FileName.dateTime(started().localTime())
                + StringTo.nonNullString(file.extension())).withoutOverwriting();
        var console = Console.get();
        console.printLine("Creating new FileLog output file: " + newFile);
        var folder = newFile.parent();
        console.printLine("Pruning files older than $ from: $", maximumLogFileAge, folder);
        folder.files(file -> file.isOlderThan(maximumLogFileAge)).forEach(at ->
        {
            console.printLine("Removed $", at);
            at.delete();
        });
        return newFile;
    }
}

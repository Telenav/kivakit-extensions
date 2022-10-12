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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.core.collections.ConvertingVariableMap;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.conversion.core.value.BytesConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.logs.file.internal.lexakai.DiagramLogsFile;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.core.string.StringConversions.toNonNullString;
import static com.telenav.kivakit.filesystem.File.parseFile;
import static com.telenav.kivakit.resource.FileName.fileNameForDateTime;

/**
 * A {@link Log} service provider that logs messages to text file(s). Configuration occurs via the command line with
 * key/value pairs stored in the KIVAKIT_LOG environment variable.
 *
 * <p><b>Configuration</b></p>
 *
 * <ul>
 *     <li><i>file</i> - The output file</li>
 *     <li><i>maximum-age</i> - The maximum age of log files (see {@link Duration} for valid values)</li>
 *     <li><i>maximum-size</i> - The maximum size (see {@link Bytes} for valid values)</li>
 *     <li><i>rollover</i> -Rollover period (none, daily or hourly)</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <pre>-DKIVAKIT_LOG="File level=Warning file=~/logs/log.txt rollover=daily maximum-age=\"1 month\" maximum-size=100M"</pre>
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsFile.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class FileLog extends BaseRolloverTextLog
{
    /** The file to write to */
    private File file;

    /** The maximum age for log files in the folder where the log file exists */
    private Duration maximumLogFileAge;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(VariableMap<String> properties)
    {
        super.configure(properties);

        var path = properties.get("file");
        if (path != null)
        {
            try
            {
                file = parseFile(consoleListener(), path);

                var rollover = properties.get("rollover");
                if (rollover != null)
                {
                    rollover(Rollover.valueOf(rollover.toUpperCase()));
                }

                var converter = new ConvertingVariableMap(consoleListener(), properties);
                maximumLogFileAge = converter.get("maximum-age", DurationConverter.class, Duration.FOREVER);
                maximumLogSize(converter.get("maximum-size", BytesConverter.class, Bytes.MAXIMUM));
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected OutputStream newOutputStream()
    {
        return newFile().openForWriting();
    }

    private File newFile()
    {
        var newFile = parseFile(consoleListener(), file.withoutExtension() + "-" + fileNameForDateTime(started().asLocalTime())
                + toNonNullString(file.extension())).withoutOverwriting();
        console().println("Creating new FileLog output file: " + newFile);
        var folder = newFile.parent();
        console().println("Pruning files older than $ from: $", maximumLogFileAge, folder);
        folder.files(resource -> ((File) resource).lastModified().isOlderThan(maximumLogFileAge)).forEach(at ->
        {
            console().println("Removed $", at);
            at.delete();
        });
        return newFile;
    }
}

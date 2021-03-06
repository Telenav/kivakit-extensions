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

import com.telenav.kivakit.core.io.ByteSizedOutputStream;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.logs.file.lexakai.DiagramLogsFile;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.io.OutputStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Base class for rollover text logs such as {@link FileLog}. Accepts a {@link #maximumLogSize(Bytes)} and a {@link
 * #rollover(Rollover)} period and logs messages until either of these limits are reached.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsFile.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseRolloverTextLog extends BaseTextLog
{
    /**
     * Rollover period
     */
    @UmlClassDiagram(diagram = DiagramLogsFile.class)
    @LexakaiJavadoc(complete = true)
    public enum Rollover
    {
        NONE,
        DAILY,
        HOURLY
    }

    private ByteSizedOutputStream byteSizedOutputStream;

    @UmlAggregation(label = "maximum size")
    private Bytes maximumLogSize;

    private PrintWriter out;

    @UmlAggregation
    private Rollover rollover = Rollover.NONE;

    @UmlAggregation(label = "rollover time")
    private Time rolloverAt = nextRollover();

    @UmlAggregation(label = "start time")
    private Time started = Time.now();

    protected BaseRolloverTextLog()
    {
        maximumLogSize(Bytes.megabytes(50));

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            flush(Duration.ONE_MINUTE);
            out.close();
        }));
    }

    @Override
    public void closeOutput()
    {
        try
        {
            flush(Duration.seconds(10));
            out().flush();
            out().close();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void flush(Duration maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        out.flush();
    }

    public void maximumLogSize(Bytes maximumSize)
    {
        maximumLogSize = maximumSize;
    }

    @Override
    public final synchronized void onLog(LogEntry entry)
    {
        var timeToRollOver = Time.now().isAfter(rolloverAt);
        var sizeToRollOver = byteSizedOutputStream != null && byteSizedOutputStream.sizeInBytes().isGreaterThan(maximumLogSize);
        if (timeToRollOver || sizeToRollOver)
        {
            try
            {
                closeOutput();
            }
            finally
            {
                if (timeToRollOver)
                {
                    started = rolloverAt;
                    rolloverAt = nextRollover();
                }
                else
                {
                    started = Time.now();
                }
                out = null;
            }
        }

        out().println(formatted(entry));
        out().flush();
    }

    public void rollover(Rollover rollover)
    {
        this.rollover = rollover;
    }

    protected abstract OutputStream newOutputStream();

    protected Time nextRollover()
    {
        switch (rollover)
        {
            case NONE:
                return Time.now().plus(Duration.ONE_MINUTE); // Time.MAXIMUM;

            case DAILY:
                return LocalTime.now().startOfTomorrow();

            case HOURLY:
                return LocalTime.now().startOfNextHour();

            default:
                return unsupported();
        }
    }

    protected Time started()
    {
        return started;
    }

    private PrintWriter newPrintWriter()
    {
        byteSizedOutputStream = new ByteSizedOutputStream(newOutputStream());
        return new PrintWriter(byteSizedOutputStream);
    }

    private PrintWriter out()
    {
        if (out == null)
        {
            out = newPrintWriter();
        }
        return out;
    }
}

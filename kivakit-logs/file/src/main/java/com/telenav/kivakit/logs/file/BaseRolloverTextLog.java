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
import com.telenav.kivakit.core.io.ByteSizedOutputStream;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.logs.file.internal.lexakai.DiagramLogsFile;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.io.OutputStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Time.END_OF_UNIX_TIME;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.core.value.count.Bytes.megabytes;
import static com.telenav.kivakit.logs.file.BaseRolloverTextLog.Rollover.NO_ROLLOVER;

/**
 * Base class for rollover text logs such as {@link FileLog}. Accepts a {@link #maximumLogSize(Bytes)} and a
 * {@link #rollover(Rollover)} period and logs messages until either of these limits are reached.
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see FileLog
 */
@SuppressWarnings("resource") @UmlClassDiagram(diagram = DiagramLogsFile.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseRolloverTextLog extends BaseTextLog
{
    /**
     * Rollover period
     */
    @UmlClassDiagram(diagram = DiagramLogsFile.class)
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public enum Rollover
    {
        NO_ROLLOVER,
        DAILY,
        HOURLY
    }

    /** The output stream */
    private ByteSizedOutputStream byteSizedOutputStream;

    /** The maximum size for log files */
    @UmlAggregation(label = "maximum size")
    private Bytes maximumLogSize = megabytes(50);

    /** The output */
    private PrintWriter out;

    /** The rollover type */
    @UmlAggregation
    private Rollover rollover = NO_ROLLOVER;

    /** The next time to roll over */
    @UmlAggregation(label = "rollover time")
    private Time rolloverAt = nextRollover();

    /** The time this log started */
    @UmlAggregation(label = "start time")
    private Time started = now();

    protected BaseRolloverTextLog()
    {
        // Flush and close this log on VM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            flush(ONE_MINUTE);
            out.close();
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeOutput()
    {
        try
        {
            flush(seconds(30));
            out().flush();
            out().close();
        }
        catch (Exception ignored)
        {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush(Duration maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        out.flush();
    }

    /**
     * Sets the maximum log file size before pruning occurs
     */
    public void maximumLogSize(Bytes maximumSize)
    {
        maximumLogSize = maximumSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized void onLog(LogEntry entry)
    {
        var timeToRollOver = now().isAfter(rolloverAt);
        var sizeToRollOver = byteSizedOutputStream != null
                && byteSizedOutputStream.sizeInBytes().isGreaterThan(maximumLogSize);
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
                    started = now();
                }
                out = null;
            }
        }

        out().println(formatted(entry));
        out().flush();
    }

    /**
     * Sets the log rollover type
     */
    public void rollover(Rollover rollover)
    {
        this.rollover = rollover;
    }

    protected abstract OutputStream newOutputStream();

    protected Time nextRollover()
    {
        switch (rollover)
        {
            case NO_ROLLOVER:
                return END_OF_UNIX_TIME;

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

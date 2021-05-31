package com.telenav.kivakit.logs.server.session;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.resource.path.FileName;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author jonathanl (shibo)
 */
public class Session implements Comparable<Session>
{
    private String name;

    private Time started;

    private Bytes size;

    public Session(final String name, final Time started, final Bytes size)
    {
        this.name = name;
        this.started = started;
        this.size = size;
    }

    protected Session()
    {
    }

    public void addAll(final List<LogEntry> entries)
    {
        SessionStore.get().addAll(this, entries);
    }

    @Override
    public int compareTo(@NotNull final Session that)
    {
        return started.compareTo(that.started);
    }

    public List<LogEntry> entries()
    {
        return SessionStore.get().entries(this);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Session)
        {
            final Session that = (Session) object;
            return name.equals(that.name) && started.equals(that.started);
        }
        return false;
    }

    public FileName fileName()
    {
        return FileName.dateTime(started.localTime()).prefixedWith(name + "-");
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, started);
    }

    public String name()
    {
        return name;
    }

    public Session session(final FileName name)
    {
        return null;
    }

    public Bytes size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return name + " - " + started.localTime().humanizedDateTime();
    }
}

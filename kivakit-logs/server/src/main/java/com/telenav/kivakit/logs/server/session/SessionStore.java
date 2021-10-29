package com.telenav.kivakit.logs.server.session;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.serialization.core.SerializationSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.kernel.logging.logs.text.formatters.ColumnarLogFormatter.DEFAULT;
import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;
import static com.telenav.kivakit.serialization.core.SerializationSession.Type.RESOURCE;

/**
 * @author jonathanl (shibo)
 */
public class SessionStore
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final Lazy<SessionStore> store = Lazy.of(SessionStore::new);

    public static SessionStore get()
    {
        return store.get();
    }

    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    private final Map<Session, LinkedList<LogEntry>> sessionNameToEntries = new HashMap<>();

    private SessionStore()
    {
    }

    public synchronized void add(Session session)
    {
        sessions.add(session);
        sessionNameToEntries.put(session, new LinkedList<>());
    }

    public synchronized void add(Session session, byte[] bytes, ProgressReporter reporter)
    {
        add(session);
        sessionFile(session, Extension.KRYO).reader(reporter).bytes();
    }

    public synchronized void addAll(Session session, List<LogEntry> toAdd)
    {
        if (session != null)
        {
            var entries = sessionNameToEntries.get(session);
            if (entries != null)
            {
                entries.addAll(toAdd);
            }
        }
    }

    public synchronized void delete(Session session)
    {
        sessions.remove(session);
        sessionFile(session, Extension.KRYO).delete();
        sessionFile(session, Extension.TXT).delete();
    }

    /**
     * @return A copy of the list of log entries for the given session
     */
    @SuppressWarnings("unchecked")
    public synchronized LinkedList<LogEntry> entries(Session session)
    {
        if (session != null)
        {
            var entries = sessionNameToEntries.get(session);
            if (entries == null)
            {
                try (var input = sessionFile(session, Extension.KRYO).openForReading())
                {
                    var serializationSession = session();
                    var version = serializationSession.open(RESOURCE, KivaKit.get().kivakitVersion(), input);
                    DEBUG.trace("Loaded session '$' (KivaKit version $)", session, version);
                    entries = (LinkedList<LogEntry>) serializationSession.read().get();
                    sessionNameToEntries.put(session, entries);
                }
                catch (IOException ignored)
                {
                    return new LinkedList<>();
                }
            }
            return new LinkedList<>(entries);
        }

        return new LinkedList<>();
    }

    public boolean has(Session session)
    {
        return sessions().contains(session);
    }

    public synchronized void load()
    {
        sessions = new HashSet<>();
        for (var file : logFolder().files(Extension.KRYO.fileMatcher()))
        {
            var parts = file.fileName().withoutExtension(Extension.KRYO).name().split("-");
            if (parts.length == 2)
            {
                var name = parts[0];
                var time = LocalTime.parseDateTime(parts[1]);
                if (!Strings.isEmpty(name) && time != null)
                {
                    sessions.add(new Session(name, time, file.sizeInBytes()));
                }
            }
        }
    }

    public byte[] read(Session session, ProgressReporter reporter)
    {
        return sessionFile(session, Extension.KRYO).reader(reporter).bytes();
    }

    public synchronized void save(Session session)
    {
        var entries = entries(session);
        if (!entries.isEmpty())
        {
            try (var output = sessionFile(session, Extension.KRYO).openForWriting())
            {
                var serializer = session();
                serializer.open(RESOURCE, KivaKit.get().kivakitVersion(), output);
                serializer.write(new VersionedObject<>(entries));
                serializer.close();
            }
            catch (IOException ignored)
            {
            }

            try (var output = sessionFile(session, Extension.TXT).printWriter())
            {
                for (var row : entries)
                {
                    output.println(row.format(DEFAULT, WITHOUT_EXCEPTION));
                }
            }
        }
    }

    public Set<Session> sessions()
    {
        return new HashSet<>(sessions);
    }

    private Folder logFolder()
    {
        return Folder.kivakitCache()
                .folder("logs")
                .mkdirs();
    }

    private SerializationSession session()
    {
        return SerializationSession.threadLocal(DEBUG.listener());
    }

    private File sessionFile(Session session, Extension extension)
    {
        return logFolder().file(session.fileName().withExtension(extension));
    }
}

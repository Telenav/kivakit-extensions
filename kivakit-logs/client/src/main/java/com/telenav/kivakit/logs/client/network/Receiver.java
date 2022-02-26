package com.telenav.kivakit.logs.client.network;

import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.logs.server.session.Session;
import com.telenav.kivakit.logs.server.session.SessionStore;
import com.telenav.kivakit.serialization.core.SerializationSession;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

import static com.telenav.kivakit.logs.client.network.Receiver.State.RUNNING;
import static com.telenav.kivakit.logs.client.network.Receiver.State.STOPPED;
import static com.telenav.kivakit.logs.client.network.Receiver.State.STOPPING;
import static com.telenav.kivakit.serialization.core.SerializationSession.Type.CLIENT;

/**
 * @author jonathanl (shibo)
 */
public class Receiver extends BaseRepeater implements Stoppable
{
    enum State
    {
        RUNNING,
        STOPPING,
        STOPPED
    }

    private final Debug DEBUG = new Debug(this);

    private State state;

    private final CompletionLatch stopping = new CompletionLatch();

    @Override
    public boolean isRunning()
    {
        return state == RUNNING;
    }

    /**
     * Reads handshake version from the given input stream. If it is compatible, reads the application from the server,
     * creates a new session for the application and passes it to the new session listener. Then reads log entries until
     * the connector signals that it is disconnecting.
     */
    public void receive(Connection connection,
                        Consumer<Session> newSessionListener,
                        Consumer<VersionedObject<?>> objectListener)
    {
        // Create a serializer and read the framework version from the server
        var serializationSession = SerializationSession.threadLocal(this);
        var version = serializationSession.open(CLIENT, KivaKit.get().kivakitVersion(), connection.input());

        // and if we are compatible with it,
        if (version.isOlderThanOrEqualTo(KivaKit.get().kivakitVersion()))
        {
            var port = connection.port();
            narrate("Handshaking with $", port);

            // read the server's application name,
            var application = serializationSession.read().get().toString();

            // create a new session for the application and give it to the listener
            var session = new Session(application, Time.now(), null);
            newSessionListener.accept(session);

            // then loop until we are told to stop,
            narrate("Receiving data from $ - $ (version $)", port, session.name(), version);
            stopping.reset();
            state = RUNNING;
            while (state == RUNNING)
            {
                try
                {
                    // reading the next entry,
                    var versionedObject = serializationSession.read();
                    if (versionedObject != null)
                    {
                        // and giving it to the object listener
                        objectListener.accept(versionedObject);
                    }
                }
                catch (Exception e)
                {
                    // until something goes wrong.
                    warning(e, "Connection broken");
                }
            }
            state = STOPPED;
            stopping.completed();
        }
        else
        {
            warning("Don't know how to talk to a server log of version $", version);
        }

        IO.close(connection.input());
    }

    @Override
    public void stop(LengthOfTime wait)
    {
        if (state == RUNNING)
        {
            state = STOPPING;
            stopping.waitForCompletion(wait);
        }
    }

    @SuppressWarnings("unchecked")
    public void synchronizeSessions(SerializationSession serializationSession)
    {
        // Read the sessions that the server has,
        Set<Session> serverSessions = (Set<Session>) serializationSession.read().get();

        // determine which sessions the server has that we still need to download,
        var desiredSessions = new ArrayList<Session>();
        for (var session : serverSessions)
        {
            if (!SessionStore.get().has(session))
            {
                desiredSessions.add(session);
            }
        }

        // tell the server which sessions we desire,
        serializationSession.write(new VersionedObject<>(desiredSessions));

        // then add each session to the cache
        for (var session : desiredSessions)
        {
            VersionedObject<byte[]> bytes = serializationSession.read();
            SessionStore.get().add(session, bytes.get(), ProgressReporter.NULL);
        }
    }
}

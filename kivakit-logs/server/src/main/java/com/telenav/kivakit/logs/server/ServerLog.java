////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.server;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.kernel.language.io.ProgressiveOutput;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.kernel.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.logs.server.session.Session;
import com.telenav.kivakit.logs.server.session.SessionStore;
import com.telenav.kivakit.network.socket.server.ConnectionListener;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.ServiceMetadata;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.client.ServiceRegistryClient;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.kernel.language.vm.KivaKitShutdownHook.Order.LAST;
import static com.telenav.kivakit.serialization.core.SerializationSession.Type.CLIENT;
import static com.telenav.kivakit.serialization.core.SerializationSession.Type.SERVER;

public class ServerLog extends BaseTextLog
{
    public static final ServiceType SERVER_LOG = new ServiceType("kivakit-server-log");

    private static final ConsoleLogger LOGGER = new ConsoleLogger();

    private static final Lazy<ServerLog> singleton = Lazy.of(ServerLog::new);

    public static ServerLog get()
    {
        return singleton.get();
    }

    private int port;

    private final LinkedList<LogEntry> entries = new LinkedList<>();

    private Maximum maximumEntries = Maximum.maximum(20_000);

    private SerializationSession serializer;

    private final Time started = Time.now();

    private final Monitor serializationLock = new Monitor();

    private final Lazy<Session> session = Lazy.of(() ->
    {
        var application = Application.get();
        if (application != null)
        {
            var session = new Session(application.name(), started, null);
            SessionStore.get().add(session);
            return session;
        }
        return null;
    });

    public ServerLog()
    {
        KivaKitShutdownHook.register(LAST, () -> SessionStore.get().save(session.get()));

        ServerLogProject.get().initialize();

        var client = LOGGER.listenTo(new ServiceRegistryClient());
        var metadata = new ServiceMetadata()
                .kivakitVersion(KivaKit.get().kivakitVersion())
                .description("KivaKit server log")
                .version(KivaKit.get().projectVersion());
        var service = client.register(Scope.network(), SERVER_LOG, metadata);
        if (service.failed())
        {
            fail("Unable to register server log: $", service.get());
        }
        else
        {
            port = service.get().port().number();
        }
    }

    @Override
    public void configure(VariableMap<String> properties)
    {
        super.configure(properties);

        var maximum = properties.get("maximum-entries");
        if (maximum != null)
        {
            maximumEntries = Maximum.parseMaximum(Listener.console(), maximum);
        }
        listen(Progress.create(LOGGER, "bytes"));
    }

    public ServerLog listen(ProgressReporter reporter)
    {
        LOGGER.listenTo(new ConnectionListener(port(), Maximum.maximum(8))).listen(socket -> handleRequest(socket, reporter));
        return this;
    }

    @Override
    public String name()
    {
        return "Server";
    }

    @Override
    public synchronized void onLog(LogEntry newEntry)
    {
        // While there are too many log entries
        while (entries.size() > maximumEntries.asInt())
        {
            // remove the first one
            entries.removeFirst();
        }

        // then add the new entry
        entries.add(newEntry);
        var store = SessionStore.get();
        store.addAll(session.get(), Collections.singletonList(newEntry));

        // and if there is a serializer to write to
        synchronized (serializationLock)
        {
            if (serializer != null)
            {
                // go through each entry to send
                for (var entry : entries)
                {
                    try
                    {
                        // format the message because we can't serialize arbitrary objects,
                        entry.formattedMessage();

                        // and send the entry
                        serializer.write(new VersionedObject<>(entry));
                        serializer.flush();
                    }
                    catch (Exception e)
                    {
                        LOGGER.warning(e, "Unable to send log entry $", entry);
                        closeConnection();
                        return;
                    }
                }
            }

            // Entries have been sent so clear the list
            entries.clear();
        }
    }

    public int port()
    {
        return port;
    }

    public void synchronizeSessions(SerializationSession serializationSession, ProgressReporter reporter)
    {
        // Send the sessions we have to the client
        serializationSession.write(new VersionedObject<>(SessionStore.get().sessions()));

        // then read back the sessions that the client wants sent
        VersionedObject<List<Session>> sessionsToSend = serializationSession.read();

        // then send each desired session back to the client
        for (var session : sessionsToSend.get())
        {
            serializationSession.write(new VersionedObject<>(SessionStore.get().read(session, reporter)));
        }
    }

    private void closeConnection()
    {
        synchronized (serializationLock)
        {
            if (serializer != null)
            {
                serializer.close();
                serializer = null;
            }
        }
    }

    /**
     * @param socket The server socket
     * @param reporter The reporter to call during connection and synchronization of a new session. This can take a
     * while because the client can download logs.
     */
    private void handleRequest(Socket socket, ProgressReporter reporter)
    {
        // Close any existing connection,
        closeConnection();

        try
        {
            // then open the socket output stream
            var input = socket.getInputStream();
            var output = socket.getOutputStream();
            if (input != null && output != null)
            {
                // layer in progress reporting
                input = new ProgressiveInput(input, reporter);
                output = new ProgressiveOutput(output, reporter);

                // get the set of sessions the log has stored,
                var store = SessionStore.get();
                store.load();

                synchronized (serializationLock)
                {
                    // Create a serializer and start writing to the connection
                    var serializer = SerializationSession.threadLocal(LOGGER);
                    serializer.open(CLIENT, KivaKit.get().kivakitVersion(), input);
                    serializer.open(SERVER, KivaKit.get().kivakitVersion(), output);

                    // then send the client our application name
                    serializer.write(new VersionedObject<>(Application.get().name()));

                    // and synchronize sessions with it
                    synchronizeSessions(serializer, reporter);

                    // then flush the serializer
                    serializer.flush();

                    // tell the progress reporter that the initialization process is done
                    reporter.end();

                    // Next, make the serializer available to the log for sending entries.
                    this.serializer = serializer;

                    // and start the health reporting thread
                    LOGGER.listenTo(new KivaKitThread("Health", () ->
                    {
                        // that loops
                        while (true)
                        {
                            // and every 15 seconds
                            Duration.seconds(15).sleep();
                            try
                            {
                                synchronized (serializationLock)
                                {
                                    // sends a health report on the JVM
                                    serializer.write(new VersionedObject<>(new JavaVirtualMachineHealth()));
                                }
                            }
                            catch (Exception e)
                            {
                                break;
                            }
                        }
                    })).start();
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.warning(e, "Socket connection failed");
        }
    }
}

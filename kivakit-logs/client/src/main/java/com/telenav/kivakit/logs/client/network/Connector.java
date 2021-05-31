package com.telenav.kivakit.logs.client.network;

import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.network.core.Port;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.telenav.kivakit.logs.client.network.Connector.State.CONNECTED;
import static com.telenav.kivakit.logs.client.network.Connector.State.CONNECTING;
import static com.telenav.kivakit.logs.client.network.Connector.State.DISCONNECTED;
import static com.telenav.kivakit.logs.client.network.Connector.State.DISCONNECTING;
import static com.telenav.kivakit.logs.client.network.Connector.State.STOPPED_CONNECTING;
import static com.telenav.kivakit.logs.client.network.Connector.State.STOP_CONNECTING;

/**
 * @author jonathanl (shibo)
 */
public class Connector extends BaseRepeater
{
    private static final Duration MAXIMUM_WAIT_TIME = Duration.seconds(45);

    private static final Duration CONNECTION_RETRY_INTERVAL = Duration.seconds(1);

    public enum State
    {
        /** The connector is trying to establish a connection */
        CONNECTING,

        /** A connection is established */
        CONNECTED,

        /** The connector is attempting to disconnect */
        DISCONNECTING,

        /** The auto-connector should stop trying to connect */
        STOP_CONNECTING,

        /** The auto-connector has stopped trying to connect */
        STOPPED_CONNECTING,

        /** There is no longer any connection */
        DISCONNECTED
    }

    /** Background thread that tries to connect repeatedly */
    private KivaKitThread autoConnectThread;

    /** The state machine for this connector */
    private final StateMachine<State> state = new StateMachine<>(DISCONNECTED);

    /** The receiver that handles the connection and receives log entries and other information */
    private final Receiver receiver;

    /** Called back when the connection state changes */
    private final Consumer<State> stateListener;

    /** Called back when a connection is established */
    private final Consumer<Connection> connectionListener;

    /** Any currently live connection */
    private Connection connection;

    /** The port we are trying to connect to (only for user feedback) */
    private Port connectingTo;

    public Connector(final Receiver receiver,
                     final Consumer<State> stateListener,
                     final Consumer<Connection> connectionListener)
    {
        this.receiver = receiver;
        this.stateListener = stateListener;
        this.connectionListener = connectionListener;
    }

    /**
     * Tries to connect to the given port on a background thread until a connection can be made
     *
     * @param port The port to connect to
     */
    public void connect(final Port port)
    {
        // If the port's host is resolvable,
        if (port.host().isResolvable())
        {
            // start trying to connect to the port on a background thread
            listenTo(KivaKitThread.run("LogConnector", () -> autoConnect(port)));
        }
        else
        {
            // otherwise, report the issue
            problem("Cannot resolve host '$'", port.host());
        }
    }

    public Port connectedPort()
    {
        return connection == null ? null : connection.port();
    }

    /**
     * Disconnect from any server we might be connected to
     */
    public void disconnect()
    {
        // If we aren't already disconnected or disconnecting
        if (state.isNot(DISCONNECTED) && state.isNot(DISCONNECTING))
        {
            // then we are now disconnecting
            final var was = state(DISCONNECTING);

            // and there is a connector trying to connect,
            if (autoConnectThread != null)
            {
                // wait until the thread is either connected or it has stopped trying to connect
                final Predicate<State> exited = state -> state == CONNECTED || state == STOPPED_CONNECTING;
                state.waitFor(exited, MAXIMUM_WAIT_TIME, autoConnectThread.interrupt());
            }

            // If we were trying to connect
            if (was == CONNECTING)
            {
                // wait for the receiveLogEntries method to exit and the Log-AutoConnector thread
                // to exit when it sees that auto-connecting is false
                narrate("Waiting for receiver to stop");
                receiver.stop(MAXIMUM_WAIT_TIME);
            }

            connection = null;
            state(DISCONNECTED);
        }
    }

    public boolean isConnected()
    {
        return state.is(CONNECTED);
    }

    /**
     * Drops any existing connection and tries forever to establish a new connection to the given port
     */
    private synchronized void autoConnect(final Port port)
    {
        narrate("Attempting to connect to $...", port);

        // Disconnect (stopping any existing auto-connect thread)
        disconnect();

        // then start auto-connecting on a new thread.
        state(CONNECTING);
        connectingTo = port;
        autoConnectThread = listenTo(KivaKitThread.run("LogAutoConnector", () ->
        {
            // Loop while we are not yet connected and we haven't been told to give up
            while (state.isNot(CONNECTED) && state.isNot(STOP_CONNECTING))
            {
                // get a connection to the given port,
                try (final InputStream input = retryUntilConnected(port))
                {
                    // given the connection listener a new connection
                    connection = new Connection(port, input);
                    connectionListener.accept(connection);

                    // and we are connected
                    state(CONNECTED);
                }
                catch (final Exception e)
                {
                    problem(e, "Connection failed");
                }
            }

            // If we had to be told to stop connecting
            if (state.is(STOP_CONNECTING))
            {
                // then we have stopped trying to connect
                state.is(STOPPED_CONNECTING);
            }

            // and we are no longer trying to connect to a port
            connectingTo = null;
        }));
    }

    /**
     * Tries to open the given port every second until it succeeds
     *
     * @return An input stream from the given port
     */
    private InputStream retryUntilConnected(final Port port)
    {
        while (state.is(CONNECTING))
        {
            try
            {
                return port.open();
            }
            catch (final Exception ignored)
            {
            }
            CONNECTION_RETRY_INTERVAL.sleep();
        }
        return null;
    }

    private State state(final State newState)
    {
        final var previous = state.at();
        state.transitionTo(newState);
        switch (newState)
        {
            case CONNECTING:
                narrate("Connecting to $", connectingTo);
                break;

            case CONNECTED:
                narrate("Connected to $", connectedPort());
                break;

            case DISCONNECTING:
                narrate("Disconnecting...");
                break;

            case DISCONNECTED:
                narrate("Disconnected");
                break;
        }
        stateListener.accept(newState);
        return previous;
    }
}

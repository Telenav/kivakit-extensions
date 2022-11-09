package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.conversion.core.language.object.ConvertedProperty;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.thread.StateMachine;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.settings.stores.zookeeper.converters.CreateModeConverter;
import telenav.third.party.zookeeper.CreateMode;
import telenav.third.party.zookeeper.KeeperException;
import telenav.third.party.zookeeper.WatchedEvent;
import telenav.third.party.zookeeper.Watcher;
import telenav.third.party.zookeeper.ZooKeeper;
import telenav.third.party.zookeeper.data.ACL;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.io.IO.close;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;
import static com.telenav.kivakit.core.path.StringPath.stringPath;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Frequency.every;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static telenav.third.party.zookeeper.CreateMode.PERSISTENT;

/**
 * Maintains a connection to zookeeper and performs operations using that connection
 *
 * <p><b>Reading and Writing</b></p>
 *
 * <ul>
 *     <li>{@link #create(StringPath, byte[], List, CreateMode)} - Creates a node at the given path</li>
 *     <li>{@link #delete(StringPath)} - Deletes the node at the given path</li>
 *     <li>{@link #read(StringPath)} - Reads the data from the node at the given path</li>
 *     <li>{@link #write(StringPath, byte[])} - Writes the given data to the node at the given path</li>
 *     <li>{@link #children(StringPath)} - Returns the names of all children of the node at the given path</li>
 * </ul>
 *
 * <p><b>Watching for Changes</b></p>
 *
 * <ul>
 *     <li>{@link #ZookeeperConnection(ZookeeperConnectionListener, ZookeeperChangeListener)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see CreateMode
 * @see ACL
 * @see ZookeeperChangeListener
 */
@SuppressWarnings("resource")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ZookeeperConnection extends BaseComponent implements Watcher, TryTrait
{
    /** State of this settings store */
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE,
                 audience = AUDIENCE_INTERNAL)
    private enum State
    {
        CONNECTED,
        DISCONNECTED
    }

    /**
     * Functional interface to {@link ZookeeperChangeListener} methods
     */
    @FunctionalInterface
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE,
                 audience = AUDIENCE_INTERNAL)
    interface ZookeeperListenerMethod
    {
        /**
         * Called with the path when a Zookeeper notification is processed
         *
         * @param path The Zookeeper path
         */
        void on(StringPath path);
    }

    /**
     * Settings for this Zookeeper connection
     */
    public static class Settings
    {
        /** Comma separated list of ports to use when connecting to Zookeeper */
        @ConvertedProperty(Port.Converter.class)
        String ports;

        /** The maximum timeout when connecting to Zookeeper */
        @ConvertedProperty(DurationConverter.class)
        Duration timeout;

        /** The default kind of data accessed by this Zookeeper connection (see {@link CreateMode}) */
        @ConvertedProperty(CreateModeConverter.class)
        CreateMode createMode = PERSISTENT;
    }

    /** Listener to call as Zookeeper reports changes */
    private final ZookeeperChangeListener changeListener;

    /** Any connecting zookeeper instance */
    private transient ZooKeeper connectingZookeeper;

    /** Listener to call as Zookeeper connects and disconnects */
    private final ZookeeperConnectionListener connectionListener;

    /** Code to run when Zookeeper connects */
    private Runnable onConnection;

    /** Connection state */
    private final StateMachine<State> state = new StateMachine<>(State.DISCONNECTED);

    /** Active watchers */
    private final Map<StringPath, Watcher> watchers = new HashMap<>();

    /** Any connected zookeeper instance */
    private transient ZooKeeper zookeeper;

    public ZookeeperConnection(ZookeeperConnectionListener connectionListener, ZookeeperChangeListener changeListener)
    {
        this.connectionListener = connectionListener;
        this.changeListener = changeListener;

        connect();
    }

    /**
     * Returns the names of the child nodes of the node at the given path
     */
    public StringList children(StringPath path)
    {
        var children = tryCatch(() -> new StringList(zookeeper().getChildren(path.join(), false)), "Unable to get children of: $", path);

        trace("Found $ children at $: $", children.count(), path, children);

        return children;
    }

    /**
     * Recursively creates the given node path using the given {@link ACL}s and {@link CreateMode}. The final node in
     * the path is then assigned the given data.
     *
     * @return The path that was created, or null if no path could be created
     */
    public StringPath create(StringPath path, byte[] data, List<ACL> acl, CreateMode mode)
    {
        // If there is a parent node,
        var parent = path.parent();
        if (parent != null && (!parent.isEmpty() && !parent.isRoot()))
        {
            // create the parent first,
            create(parent, new byte[0], acl, mode);
        }

        try
        {
            // then create the node at the given path.
            var newPath = zookeeper().create(path.join(), data, acl, mode);
            if (newPath != null)
            {
                trace("Created: $", newPath);
                return parseStringPath(this, newPath, "/", "/");
            }
        }
        catch (KeeperException.NodeExistsException ignored)
        {
            trace("Node already exists: $", path);
            return path;
        }
        catch (Exception e)
        {
            problem(e, "Could not create $", path);
        }

        problem("Could not create $", path);
        return null;
    }

    /**
     * Returns the default create mode for this connection
     */
    public CreateMode defaultCreateMode()
    {
        return settings().createMode;
    }

    /**
     * Returns true if the given node path was deleted
     */
    public boolean delete(StringPath path)
    {
        try
        {
            zookeeper().delete(path.join(), -1);
            trace("Deleted: $", path);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Could not delete: $", path);
            return false;
        }
    }

    /**
     * Returns true if the given node path exists
     */
    public boolean exists(StringPath path)
    {
        try
        {
            var exists = zookeeper().exists(path.join(), false) != null;
            trace((exists ? "Node exists: " : "Node does not exist: ") + path);
            return exists;
        }
        catch (Exception e)
        {
            problem(e, "Could not determine existence: $", path);
            return false;
        }
    }

    /**
     * Returns true if this Zookeeper connection is connected and ready
     */
    public boolean isConnected()
    {
        return zookeeper() != null;
    }

    /**
     * Sets code to call when Zookeeper connects
     *
     * @param code The code to call
     */
    public void onConnection(Runnable code)
    {
        this.onConnection = code;
    }

    /**
     * <b>Zookeeper API</b>
     *
     * <p>
     * Handle Zookeeper events. If the event is of type None and the state is SyncConnected or Disconnected, the state
     * of the connection is updated, otherwise the appropriate method of {@link ZookeeperChangeListener} is called.
     * </p>
     */
    @Override
    public void process(WatchedEvent event)
    {
        trace("Received event: $", event);

        switch (event.getType())
        {
            case NodeCreated:
                invokeListenerMethod(event, changeListener::onNodeCreated);
                break;

            case NodeDeleted:
                invokeListenerMethod(event, changeListener::onNodeDeleted);
                break;

            case NodeDataChanged:
                invokeListenerMethod(event, changeListener::onNodeDataChanged);
                break;

            case NodeChildrenChanged:
                invokeListenerMethod(event, changeListener::onNodeChildrenChanged);
                break;

            case None:
                switch (event.getState())
                {
                    case SyncConnected -> onConnected();
                    case Disconnected -> onDisconnected();
                }
        }
    }

    /**
     * Returns the data at the given node path
     */
    public byte[] read(StringPath path)
    {
        try
        {
            // read and serialize the altered settings object,
            var data = zookeeper().getData(path.join(), false, null);
            trace("Read $: $", bytes(data), path);
            return data;
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return null;
        }
    }

    /**
     * Returns the Zookeeper root path
     */
    public StringPath root()
    {
        return stringPath(List.of()).withRoot("/");
    }

    /**
     * Watches the given path for changes, which will be given to the {@link ZookeeperChangeListener}. If the connection
     * is dropped, the watch will be automatically re-established when the connection is established again.
     */
    public void watch(StringPath path)
    {
        try
        {
            // Register both a data watch and a child watch for the given path
            zookeeper().getData(path.join(), true, null);
            zookeeper().getChildren(path.join(), true);

            // and remember the watch in case we get disconnected.
            watchers.put(path, this);
        }
        catch (Exception ignored)
        {
        }
    }

    /**
     * Starts watching the Zookeeper root
     */
    public void watchRoot()
    {
        watch(root());
    }

    /**
     * Returns the active watchers for this connection
     */
    public Map<StringPath, Watcher> watchers()
    {
        return watchers;
    }

    /**
     * Writes the given data to the node at the given path
     */
    public boolean write(StringPath path, byte[] data)
    {
        try
        {
            var success = zookeeper().setData(path.join(), data, -1) != null;
            if (success)
            {
                trace("Wrote $: $", bytes(data), path);
            }
            return success;
        }
        catch (Exception e)
        {
            problem(e, "Could not write $ to: $", bytes(data), path);
            return false;
        }
    }

    /**
     * Returns the Zookeeper instance for this connection
     */
    public synchronized ZooKeeper zookeeper()
    {
        if (zookeeper == null)
        {
            throw new IllegalStateException("Zookeeper not ready");
        }
        return zookeeper;
    }

    /**
     * Connects to Zookeeper on a background thread
     */
    @SuppressWarnings("InfiniteLoopStatement")
    private void connect()
    {
        while (true)
        {
            synchronized (this)
            {
                if (connectingZookeeper == null)
                {
                    try
                    {
                        // Start Zookeeper with this object as the watcher,
                        this.connectingZookeeper = new ZooKeeper(settings().ports, (int) settings().timeout.asMilliseconds(), this);
                    }
                    catch (Exception e)
                    {
                        transmit(new Warning("Unable to connect to zookeeper: $", settings().ports)
                                .maximumFrequency(every(minutes(1.5))));
                    }
                }
            }

            seconds(15).sleep();
        }
    }

    /**
     * Calls the given listener method of the {@link ZookeeperChangeListener} interface
     *
     * @param event The Zookeeper event
     * @param listener The listener to call
     */
    private void invokeListenerMethod(WatchedEvent event, ZookeeperListenerMethod listener)
    {
        if (listener != null)
        {
            var path = path(event);
            listener.on(path);
            watch(path);
        }
        else
        {
            problem("No listener method to invoke");
        }
    }

    /**
     * Called when the connection becomes connected
     */
    private void onConnected()
    {
        // If we can transition to the connected state,
        if (state.transition(State.DISCONNECTED, State.CONNECTED))
        {
            // assign the connecting Zookeeper instance to the zookeeper field.
            trace("Connected");
            synchronized (this)
            {
                zookeeper = connectingZookeeper;
            }

            if (connectionListener != null)
            {
                connectionListener.onConnected();
            }

            onConnection.run();

            // If we are reconnecting after a connection failure, go through each watched path,
            for (var path : watchers.keySet())
            {
                // and add it back
                watch(path);
            }
        }
    }

    /**
     * Called when disconnected from Zookeeper
     */
    private void onDisconnected()
    {
        if (state.transition(State.CONNECTED, State.DISCONNECTED))
        {
            close(this, zookeeper);
            zookeeper = null;
            connectingZookeeper = null;

            if (connectionListener != null)
            {
                connectionListener.onDisconnected();
            }

            trace("Disconnected");
        }
    }

    /**
     * Returns a StringPath for the given event path
     */
    @Nullable
    private StringPath path(WatchedEvent event)
    {
        return stringPath(stripLeading(event.getPath(), "/")).withRoot("/");
    }

    /**
     * Returns the settings for this connection
     */
    private Settings settings()
    {
        return require(Settings.class);
    }
}

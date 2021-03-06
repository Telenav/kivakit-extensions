package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.conversion.core.language.object.KivaKitConverted;
import com.telenav.kivakit.conversion.core.time.DurationConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.thread.StateMachine;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.settings.stores.zookeeper.converters.CreateModeConverter;
import kivakit.merged.zookeeper.CreateMode;
import kivakit.merged.zookeeper.KeeperException;
import kivakit.merged.zookeeper.WatchedEvent;
import kivakit.merged.zookeeper.Watcher;
import kivakit.merged.zookeeper.ZooKeeper;
import kivakit.merged.zookeeper.data.ACL;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kivakit.merged.zookeeper.CreateMode.PERSISTENT;

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
public class ZookeeperConnection extends BaseComponent implements Watcher
{
    /** State of this settings store */
    private enum State
    {
        CONNECTED,
        DISCONNECTED
    }

    /**
     * Functional interface to {@link ZookeeperChangeListener} methods
     */
    @FunctionalInterface
    interface ListenerMethod
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
        @KivaKitConverted(Port.Converter.class)
        String ports;

        /** The maximum timeout when connecting to Zookeeper */
        @KivaKitConverted(DurationConverter.class)
        Duration timeout;

        /** The default kind of data accessed by this Zookeeper connection (see {@link CreateMode}) */
        @KivaKitConverted(CreateModeConverter.class)
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
     * @return The names of the child nodes of the node at the given path
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
                return StringPath.parseStringPath(this, newPath, "/", "/");
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
     * @return The default create mode for this connection
     */
    public CreateMode defaultCreateMode()
    {
        return settings().createMode;
    }

    /**
     * @return True if the given node path was deleted
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
     * @return True if the given node path exists
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
     * @return True if this Zookeeper connection is connected and ready
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
                    case SyncConnected:
                        onConnected();
                        break;

                    case Disconnected:
                        onDisconnected();
                        break;
                }
        }
    }

    /**
     * @return The data at the given node path
     */
    public byte[] read(StringPath path)
    {
        try
        {
            // read and serialize the altered settings object,
            var data = zookeeper().getData(path.join(), false, null);
            trace("Read $: $", Bytes.bytes(data), path);
            return data;
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return null;
        }
    }

    /**
     * @return The Zookeeper root path
     */
    public StringPath root()
    {
        return StringPath.stringPath(List.of()).withRoot("/");
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
     * @return The active watchers for this connection
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
                trace("Wrote $: $", Bytes.bytes(data), path);
            }
            return success;
        }
        catch (Exception e)
        {
            problem(e, "Could not write $ to: $", Bytes.bytes(data), path);
            return false;
        }
    }

    /**
     * @return The Zookeeper instance for this connection
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
        // Start a background thread to keep re-connecting to Zookeeper
        KivaKitThread.run("Zookeeper Connector", () ->
        {
            while (true)
            {
                synchronized (ZookeeperConnection.this)
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
                            warning(Frequency.every(Duration.minutes(1.5)), "Unable to connect to zookeeper: $", settings().ports);
                        }
                    }
                }

                Duration.seconds(15).sleep();
            }
        });
    }

    /**
     * Calls the given listener method of the {@link ZookeeperChangeListener} interface
     *
     * @param event The Zookeeper event
     * @param method The method to call
     */
    private void invokeListenerMethod(WatchedEvent event, ListenerMethod method)
    {
        if (method != null)
        {
            var path = path(event);
            method.on(path);
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
            IO.close(zookeeper);
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
     * @return A StringPath for the given event path
     */
    @Nullable
    private StringPath path(final WatchedEvent event)
    {
        return StringPath.stringPath(Strip.leading(event.getPath(), "/")).withRoot("/");
    }

    /**
     * @return The settings for this connection
     */
    private Settings settings()
    {
        return require(Settings.class);
    }
}

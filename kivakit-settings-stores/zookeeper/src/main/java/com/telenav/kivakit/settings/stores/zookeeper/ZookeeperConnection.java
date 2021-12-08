package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.settings.stores.zookeeper.converters.CreateModeConverter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static org.apache.zookeeper.CreateMode.PERSISTENT;

/**
 * Maintains a connection to zookeeper and performs operations using that connection
 *
 * <p><b>Reading and Writing</b></p>
 *
 * <ul>
 *     <li>{@link #create(StringPath, List, CreateMode)} - Creates a node at the given path</li>
 *     <li>{@link #delete(StringPath)} - Deletes the node at the given path</li>
 *     <li>{@link #read(StringPath)} - Reads the data from the node at the given path</li>
 *     <li>{@link #write(StringPath, byte[])} - Writes the given data to the node at the given path</li>
 *     <li>{@link #children(StringPath)} - Returns the paths of all children of the node at the given path</li>
 * </ul>
 *
 * <p><b>Watching for Changes</b></p>
 *
 * <ul>
 *     <li>{@link #addChangeListener(ZookeeperChangeListener)} - Adds a listener to call when nodes are created, deleted and changed</li>
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
     * Settings for this Zookeeper connection
     */
    public static class Settings
    {
        /** Comma separated list of ports to use when connecting to Zookeeper */
        @KivaKitPropertyConverter(Port.Converter.class)
        String ports;

        /** The maximum timeout when connecting to Zookeeper */
        @KivaKitPropertyConverter(Duration.Converter.class)
        Duration timeout;

        /** The default kind of data accessed by this Zookeeper connection (ephemeral or persistent) */
        @KivaKitPropertyConverter(CreateModeConverter.class)
        CreateMode defaultCreateMode = PERSISTENT;
    }

    /** Any connected zookeeper instance */
    private transient ZooKeeper zookeeper;

    /** Connection state */
    private final StateMachine<State> state = new StateMachine<>(State.DISCONNECTED);

    /** Listener to call as Zookeeper reports changes */
    private ZookeeperChangeListener listener;

    /** Active watchers */
    private final Map<StringPath, Watcher> watchers = new HashMap<>();

    /**
     * Sets the change listener to call as the store changes
     */
    public void addChangeListener(ZookeeperChangeListener listener)
    {
        ensureNull(this.listener, "Cannot add more than one listener");

        this.listener = listener;
    }

    /**
     * @return The names of the child nodes of the node at the given path
     */
    public StringList children(StringPath path)
    {
        var children = new StringList();

        try
        {
            // Go through child paths of the given path,
            for (var at : zookeeper().getChildren(path.join(), this))
            {
                // and if the path is for this settings store,
                children.append(at);
            }
        }
        catch (Exception e)
        {
            problem(e, "Unable to get children of: $", path);
        }

        trace("Found $ children: $", children.count(), path);

        return children;
    }

    /**
     * Recursively creates the given node path using the given {@link ACL}s and {@link CreateMode}
     *
     * @return True if the node path was created
     */
    public boolean create(StringPath path, List<ACL> acl, CreateMode mode)
    {
        // If there is a parent node,
        var parent = path.parent();
        if (parent != null && (!parent.isEmpty() && !parent.isRoot()))
        {
            // create the parent first,
            create(parent, acl, mode);
        }

        try
        {
            // then create the node at the given path.
            if (zookeeper().create(path.join(), new byte[0], acl, mode) != null)
            {
                trace("Created: $", path);
                return true;
            }
        }
        catch (NodeExistsException ignored)
        {
            trace("Node already exists: $", path);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Could not create $", path);
        }

        problem("Could not create $", path);
        return false;
    }

    /**
     * @return The default create mode for this connection
     */
    public CreateMode defaultCreateMode()
    {
        return settings().defaultCreateMode;
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
            var exists = zookeeper().exists(path.join(), watchers.get(path)) != null;
            trace((exists ? "Exists: " : "Does not exist: ") + path);
            return exists;
        }
        catch (Exception e)
        {
            problem(e, "Could not determine existence: $", path);
            return false;
        }
    }

    /**
     * Handle Zookeeper events. If the event is of type None and the state is SyncConnected or Disconnected, the state
     * of the connection is updated, otherwise the appropriate method of {@link ZookeeperChangeListener} is called.
     */
    @Override
    public void process(WatchedEvent event)
    {
        trace("Received event: $", event);

        switch (event.getType())
        {
            case NodeCreated:
                if (listener != null)
                {
                    var path = path(event);
                    listener.onNodeCreated(path);
                    rewatch(path);
                }
                break;

            case NodeDeleted:
                if (listener != null)
                {
                    var path = path(event);
                    listener.onNodeDeleted(path);
                    rewatch(path);
                }
                break;

            case NodeDataChanged:
                if (listener != null)
                {
                    var path = path(event);
                    listener.onNodeDataChanged(path);
                    rewatch(path);
                }
                break;

            case NodeChildrenChanged:
                if (listener != null)
                {
                    var path = path(event);
                    listener.onNodeChildrenChanged(path);
                    rewatch(path);
                }
                break;

            case None:
                switch (event.getState())
                {
                    case SyncConnected:
                        connected();
                        break;

                    case Disconnected:
                        disconnected();
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
            var data = zookeeper().getData(path.join(), watchers.get(path), null);
            trace("Read $: $", Bytes.bytes(data.length), path);
            return data;
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return null;
        }
    }

    /**
     * Watches the given path for changes, which will be given to the {@link ZookeeperChangeListener} added with {@link
     * #addChangeListener(ZookeeperChangeListener)}. If the connection is dropped, the watch will be automatically
     * re-established when the connection is established again.
     */
    public void watch(StringPath path)
    {
        try
        {
            if (exists(path))
            {
                zookeeper().getData(path.join(), this, null);
                watchers.put(path, this);
            }
        }
        catch (Exception ignored)
        {
        }
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
                trace("Wrote $: $", Bytes.bytes(data.length), path);
            }
            return success;
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return false;
        }
    }

    /**
     * @return The Zookeeper instance for this connection
     */
    public ZooKeeper zookeeper()
    {
        while (zookeeper == null)
        {
            try
            {
                zookeeper = new ZooKeeper(settings().ports, (int) settings().timeout.asMilliseconds(), this);
                state.waitFor(State.CONNECTED);
                break;
            }
            catch (Exception e)
            {
                fail(e, "Unable to connect to zookeeper: $", settings().ports);
            }

            Duration.seconds(5).sleep();
        }

        return zookeeper;
    }

    /**
     * Called when the connection becomes connected
     */
    private void connected()
    {
        if (state.transition(State.DISCONNECTED, State.CONNECTED))
        {
            trace("Connected");

            // If we are reconnecting after a connection failure, go through each watched path,
            for (var path : watchers.keySet())
            {
                // and add it back
                rewatch(path);
            }
        }
    }

    /**
     * Called when disconnected from Zookeeper
     */
    private void disconnected()
    {
        if (state.transition(State.CONNECTED, State.DISCONNECTED))
        {
            IO.close(zookeeper);
            zookeeper = null;
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
     * Re-establishes any watch for the given path
     */
    private void rewatch(StringPath path)
    {
        // If there is a watcher of this path,
        if (watchers.containsKey(path))
        {
            // add it back
            watch(path);
        }
    }

    /**
     * @return The settings for this connection
     */
    private Settings settings()
    {
        return require(Settings.class);
    }
}

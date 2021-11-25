package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.threading.conditions.StateMachine;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.settings.stores.zookeeper.converters.CreateModeConverter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static org.apache.zookeeper.CreateMode.PERSISTENT;

public class ZookeeperConnection extends BaseComponent implements Watcher
{
    /** State of this settings store */
    private enum State
    {
        CONNECTING,
        CONNECTED,
        DISCONNECTED
    }

    /**
     * Settings for the store
     */
    public static class Settings
    {
        /** Comma separated ports to use when connecting to Zookeeper */
        @KivaKitPropertyConverter(Port.Converter.class)
        private String ports;

        /** THe maximum timeout when connecting to Zookeeper */
        @KivaKitPropertyConverter(Duration.Converter.class)
        private Duration timeout;

        /** The CreateMode for data in Zookeeper */
        @KivaKitPropertyConverter(CreateModeConverter.class)
        private CreateMode defaultCreateMode = PERSISTENT;
    }

    /** Any connected zookeeper instance */
    private transient ZooKeeper zookeeper;

    /** Latch that holds back callers until Zookeeper is ready */
    private final StateMachine<State> state = new StateMachine<>(State.CONNECTING);

    /** Listener to call as Zookeeper reports changes */
    private ZookeeperChangeListener listener;

    /**
     * Sets the change listener to call as the store changes
     */
    public void addChangeListener(ZookeeperChangeListener listener)
    {
        this.listener = listener;
    }

    /**
     * @return The child nodes of the given node path
     */
    public StringList children(StringPath path)
    {
        var children = new StringList();

        try
        {
            // Go through child paths of the given path,
            for (var at : zookeeper().getChildren(path.join(), null))
            {
                // and if the path is for this settings store,
                children.append(at);
            }
        }
        catch (Exception e)
        {
            problem("Unable to get children of: $", path);
        }

        return children;
    }

    /**
     * Creates the given node path using the given {@link ACL}s and {@link CreateMode}
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
                return true;
            }
        }
        catch (NodeExistsException ignored)
        {
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Could not create $", path);
        }

        problem("Could not create $", path);
        return false;
    }

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
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Could not delete: $", path);
            return false;
        }
    }

    /**
     * Called when disconnected from Zookeeper
     */
    public void disconnected()
    {
        IO.close(zookeeper);
        zookeeper = null;
        state.transitionTo(State.DISCONNECTED);
    }

    /**
     * Handle Zookeeper events. If the event is SyncConnected or Disconnected, the state of the connection is updated,
     * otherwise {@link #onEvent(WatchedEvent)} is called to dispatch the event to any {@link ZookeeperChangeListener}.
     */
    @Override
    public void process(WatchedEvent event)
    {
        switch (event.getState())
        {
            case SyncConnected:
                connected();
                return;

            case Disconnected:
                disconnected();
                return;
        }

        onEvent(event);
    }

    /**
     * @return The data at the given node path
     */
    public byte[] read(StringPath path)
    {
        try
        {
            // read and serialize the altered settings object,
            return zookeeper().getData(path.join(), null, null);
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return null;
        }
    }

    /**
     * Writes the given data to the node at the given path
     */
    public boolean write(StringPath path, byte[] data)
    {
        try
        {
            // read and serialize the altered settings object,
            return zookeeper().setData(path.join(), data, -1) != null;
        }
        catch (Exception e)
        {
            problem(e, "Could not get data for: $", path);
            return false;
        }
    }

    /**
     * @return The connected Zookeeper instance for these settings
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
     * Dispatches Zookeeper events to {@link ZookeeperChangeListener}
     */
    protected void onEvent(WatchedEvent event)
    {
        if (listener != null)
        {
            var path = StringPath.stringPath(event.getPath()).withRoot("/");

            switch (event.getType())
            {
                case NodeCreated:
                    listener.onNodeCreated(path);
                    break;

                case NodeDeleted:
                    listener.onNodeDeleted(path);
                    break;

                case NodeDataChanged:
                    listener.onNodeDataChanged(path);
                    break;
            }
        }
    }

    /**
     * Called when the connection becomes connected
     */
    private State connected()
    {
        return state.transitionTo(State.CONNECTED);
    }

    private Settings settings()
    {
        return require(Settings.class);
    }
}

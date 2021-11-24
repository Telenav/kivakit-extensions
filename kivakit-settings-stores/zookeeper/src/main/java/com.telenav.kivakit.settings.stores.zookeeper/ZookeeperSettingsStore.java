package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.BaseSettingsStore;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.configuration.settings.SettingsStore;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.kernel.language.threading.latches.InitializationLatch;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.settings.stores.zookeeper.converters.CreateModeConverter;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.SAVE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.UNLOAD;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.Watcher.Event.KeeperState.SyncConnected;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * A {@link SettingsStore} that uses Apache Zookeeper to load and save settings objects, and to update settings when the
 * Zookeeper store is changed.
 *
 * @author jonathanl (shibo)
 */
public class ZookeeperSettingsStore extends BaseSettingsStore implements RegistryTrait, Watcher
{
    public static class Settings
    {
        @KivaKitPropertyConverter(Port.Converter.class)
        private String ports;

        @KivaKitPropertyConverter(Duration.Converter.class)
        private Duration timeout;

        @KivaKitPropertyConverter(CreateModeConverter.class)
        private CreateMode createMode = PERSISTENT;

        /** Any connected zookeeper */
        private transient ZooKeeper zookeeper;

        public String ports()
        {
            return ports;
        }

        /**
         * @return The connected Zookeeper instance for these settings
         */
        public ZooKeeper zookeeper()
        {
            while (zookeeper == null)
            {
                var latch = new InitializationLatch();

                try
                {
                    zookeeper = new ZooKeeper(ports, (int) timeout.asMilliseconds(), event ->
                    {
                        if (event.getState() == SyncConnected)
                        {
                            latch.ready();
                        }
                    });
                }
                catch (Exception e)
                {
                    fail(e, "Unable to connect to zookeeper with connection path: $", ports);
                }

                latch.await();
            }

            return zookeeper;
        }
    }

    /** Settings for this store */
    private CreateMode createMode;

    public ZookeeperSettingsStore(CreateMode createMode)
    {
        this.createMode = createMode;
    }

    public ZookeeperSettingsStore()
    {
    }

    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD, SAVE);
    }

    @Override
    public void process(WatchedEvent event)
    {
        var path = StringPath.stringPath(event.getPath());
        if (path.size() > 2)
        {
            var typeName = path.get(path.size() - 2);
            var instance = path.last();
            var type = Classes.forName(typeName);
            if (type != null)
            {

                try
                {
                    switch (event.getType())
                    {
                        case NodeDeleted:
                        {
                            var object = lookup(type, InstanceIdentifier.of(instance));
                            if (object != null)
                            {
                                var settingsObject = new SettingsObject(object, type, InstanceIdentifier.of(instance));
                                onSettingsRemoved(StringPath.stringPath(event.getPath()), settingsObject);
                                Registry.of(this).unregister(object);
                            }
                            else
                            {
                                warning("No object registered for: $:$", typeName, instance);
                            }
                        }
                        break;

                        case NodeCreated:
                        case NodeDataChanged:
                        {
                            var data = zookeeper().getData(event.getPath(), null, null);
                            var object = onDeserialize(data, type);
                            var settingsObject = new SettingsObject(object, type, InstanceIdentifier.of(instance));
                            if (object != null)
                            {
                                index(settingsObject);
                            }
                            else
                            {
                                warning("Unable to update: $ ($)", typeName, instance);
                            }

                            onSettingsUpdated(StringPath.stringPath(event.getPath()), settingsObject);
                        }
                        break;
                    }
                }
                catch (Exception e)
                {
                    problem("Unable to process WatchedEvent: $", event);
                }
            }
        }
    }

    @Override
    protected boolean onDelete(SettingsObject object)
    {
        var identifier = object.identifier();

        var path = applicationPath()
                .withChild(identifier.type().getName())
                .withChild(identifier.instance().identifier());

        try
        {
            zookeeper().delete(path.join(), -1);
            onSettingsRemoved(path, object);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to save object: $", object);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T onDeserialize(byte[] data, Class<?> type)
    {
        var gson = require(GsonFactory.class).gson();
        var json = new String(data, UTF_8);
        return (T) gson.fromJson(json, type);
    }

    @Override
    protected Set<SettingsObject> onLoad()
    {
        try
        {
            var settings = new ObjectSet<SettingsObject>();

            // Go through the stored types,
            for (var typeName : zookeeper().getChildren(applicationPath().join(), null))
            {
                var type = Classes.forName(typeName);
                var typePath = applicationPath().withChild(typeName);
                for (var instance : zookeeper().getChildren(typePath.join(), null))
                {
                    var data = zookeeper().getData(typePath.withChild(instance).join(), this, null);

                    var object = onDeserialize(data, type);
                    if (object != null)
                    {
                        settings.add(new SettingsObject(object, type, InstanceIdentifier.of(instance)));
                    }
                }
            }

            return settings;
        }
        catch (Exception e)
        {
            throw problem(e, "Unable to load settings from: $", applicationPath()).asException();
        }
    }

    @Override
    protected boolean onSave(SettingsObject settings)
    {
        var identifier = settings.identifier();

        var path = StringPath.stringPath(applicationPath()
                .withChild(identifier.type().getName())
                .withChild(identifier.instance().identifier()).join("::"));

        try
        {
            mkdirs(path);
            var data = onSerialize(settings.object());
            zookeeper().setData(path.join(), data, -1);
            onSettingsUpdated(path, settings);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to save object: $", settings);
            return false;
        }
    }

    protected byte[] onSerialize(Object object)
    {
        var gson = require(GsonFactory.class).gson();
        var json = gson.toJson(object);
        return json.getBytes(UTF_8);
    }

    protected void onSettingsRemoved(StringPath path, SettingsObject settings)
    {
    }

    protected void onSettingsUpdated(StringPath path, SettingsObject settings)
    {

    }

    private StringPath applicationPath()
    {
        var application = require(Application.class);

        return StringPath.stringPath(
                "kivakit",
                String.valueOf(KivaKit.get().kivakitVersion()),
                application.name(),
                application.version().toString(),
                JavaVirtualMachine.property("user.name")).withRoot("/");
    }

    private void create(StringPath path)
    {
        try
        {
            // then create the node at the given path.
            zookeeper().create(path.join(), new byte[0], OPEN_ACL_UNSAFE, createMode());
        }
        catch (NodeExistsException ignored)
        {
        }
        catch (Exception e)
        {
            problem(e, "Could not create $", path);
        }
    }

    private CreateMode createMode()
    {
        if (createMode == null)
        {
            createMode = settings().createMode;
        }
        return createMode;
    }

    private void mkdirs(StringPath path)
    {
        // Create the parent node first,
        var parent = path.parent();
        if (parent != null && (!parent.isEmpty() && !parent.isRoot()))
        {
            mkdirs(parent);
        }

        create(path);
    }

    private Settings settings()
    {
        return require(Settings.class);
    }

    private ZooKeeper zookeeper()
    {
        return settings().zookeeper();
    }
}

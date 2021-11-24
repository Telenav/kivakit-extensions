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
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.serialization.json.GsonFactory;
import org.apache.zookeeper.CreateMode;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.SAVE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.UNLOAD;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * A {@link SettingsStore} that uses Apache Zookeeper to load and save settings objects, and to update settings when the
 * Zookeeper store is changed.
 *
 * <p><b>Creating</b></p>
 *
 * <p>
 * A {@link ZookeeperSettingsStore} can be constructed with or without a {@link CreateMode}. The connection to Zookeeper
 * is maintained by a {@link ZookeeperConnection} that is configured with {@link ZookeeperConnection.Settings}. When
 * using the kivakit <i>-deployment</i> switch, the ZookeeperConnection.properties file should look like this:
 * <pre>
 * class             = com.telenav.kivakit.settings.stores.zookeeper.ZookeeperConnection$Settings
 *
 * ports             = 127.0.0.1:2181,127.0.0.1:2182
 * timeout           = 60s
 * defaultCreateMode = PERSISTENT</pre>
 * </p>
 *
 * <p><b>Loading Settings</b></p>
 *
 * <p>
 * Once the {@link ZookeeperConnection.Settings} object is registered in the global lookup, a settings store can be
 * registered like this:
 * <pre>
 * registerSettingsIn(listenTo(new ZookeeperSettingsStore()));</pre>
 *
 * <p><b>Reacting to Changes</b></p>
 *
 * <p>
 * Because KivaKit's {@link Registry} methods pull objects from the registry, it is generally not necessary to respond
 * to Zookeeper data changes. When a node in Zookeeper that is storing a settings object changes, this settings store
 * will be updated with the new object. The next time the object is looked up, the new version will be retrieved
 * automatically.
 * </p>
 *
 * <p>
 * If this isn't sufficient, these methods can be overridden:
 * </p>
 * <ul>
 *     <li>{@link #onSettingsUpdated(StringPath, SettingsObject)} - Called when a settings object is created or updated in Zookeeper</li>
 *     <li>{@link #onSettingsDeleted(StringPath, SettingsObject)} - Called when a settings object is deleted in Zookeeper</li>
 * </ul>
 *
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class ZookeeperSettingsStore extends BaseSettingsStore implements RegistryTrait, ZookeeperChangeListener
{
    private static final String SEPARATOR = "--";

    /** Create mode for settings in this store */
    private CreateMode createMode;

    /** Connection to Zookeeper */
    private ZookeeperConnection connection = new ZookeeperConnection();

    public ZookeeperSettingsStore(CreateMode createMode)
    {
        this.createMode = createMode;
        connection.addChangeListener(this);
    }

    public ZookeeperSettingsStore()
    {
        connection.addChangeListener(this);
    }

    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD, SAVE);
    }

    @Override
    public void onNodeCreated(final StringPath path)
    {
        onNodeDataChanged(path);
    }

    @Override
    public void onNodeDataChanged(final StringPath path)
    {
        // Get the settings object type and instance referred to by the given path
        var type = settingsType(path);
        var instance = instance(path);

        try
        {
            // read and serialize the altered settings object,
            var data = connection.read(path);
            if (data != null)
            {
                var object = onDeserialize(data, type);
                if (object != null)
                {
                    // then index the object,
                    var settings = new SettingsObject(object, type, instance);
                    index(settings);

                    // propagate the change,
                    var update = propagateChangesTo();
                    if (update != null)
                    {
                        update.index(settings);
                    }

                    // and tell the subclass we updated.
                    onSettingsUpdated(path, settings);
                }
                else
                {
                    warning("Could not deserialize: $", new String(data));
                }
            }
        }
        catch (Exception e)
        {
            problem(e, "Could not update: $", path);
        }
    }

    @Override
    public void onNodeDeleted(final StringPath path)
    {
        // Get the settings object type and instance referred to by the given path
        var type = settingsType(path);
        var instance = instance(path);

        // If we can find the object,
        var object = lookup(type, instance);
        if (object != null)
        {
            // unindex it in the local store,
            var settings = new SettingsObject(object, type, instance);
            unindex(settings);

            // propagate the change,
            var update = propagateChangesTo();
            if (update != null)
            {
                update.unindex(settings);
            }

            // and notify the subclass of the deletion.
            onSettingsDeleted(path, settings);
        }
        else
        {
            warning("No object registered for: $:$", type, instance);
        }
    }

    @Override
    @MustBeInvokedByOverriders
    protected boolean onDelete(SettingsObject settings)
    {
        return connection.delete(path(settings));
    }

    @SuppressWarnings("unchecked")
    protected <T> T onDeserialize(byte[] data, Class<?> type)
    {
        var gson = require(GsonFactory.class).gson();
        var json = new String(data, UTF_8);
        return (T) gson.fromJson(json, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<SettingsObject> onLoad()
    {
        var settings = new ObjectSet<SettingsObject>();

        // Go through the child paths of the root node,
        for (var child : connection.children(StringPath.stringPath("/")).prefixedWith("/"))
        {
            // and if the path is for this settings store,
            if (child.startsWith(storePath().join(SEPARATOR)))
            {
                onNodeDataChanged(StringPath.stringPath(child));
            }
        }

        return settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onSave(SettingsObject settings)
    {
        var path = path(settings);

        try
        {
            // Create the node if necessary,
            create(path);

            // serialize the object,
            var data = onSerialize(settings.object());

            // and write the serialized data to Zookeeper.
            connection.write(path, data);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to save object: $", settings);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected byte[] onSerialize(Object object)
    {
        var gson = require(GsonFactory.class).gson();
        var json = gson.toJson(object);
        return json.getBytes(UTF_8);
    }

    protected void onSettingsDeleted(StringPath path, SettingsObject settings)
    {
    }

    protected void onSettingsUpdated(StringPath path, SettingsObject settings)
    {
    }

    private boolean create(StringPath path)
    {
        return connection.create(path, OPEN_ACL_UNSAFE, createMode());
    }

    private CreateMode createMode()
    {
        if (createMode == null)
        {
            createMode = connection.defaultCreateMode();
        }
        return createMode;
    }

    private InstanceIdentifier instance(StringPath path)
    {
        return InstanceIdentifier.of(path.last());
    }

    /**
     * Composes the Zookeeper path to the given settings object:
     *
     * <ol>
     *     <li>EPHEMERAL|PERSISTENT</li>
     *     <li>`kivakit</li>
     *     <li>kivakit-version</li>
     *     <li>user-name</li>
     *     <li>application-name</li>
     *     <li>application-version</li>
     *     <li>type-name</li>
     *     <li>instance-identifier</li>
     * </ol>
     * <pre>
     * /[EPHEMERAL|PERSISTENT]/kivakit/[kivakit-version]/[user-name]/[application-name]/[application-version]/[type-name]/[instance-identifier]</pre>
     *
     * @return A path to the given settings object
     */
    private StringPath path(SettingsObject object)
    {
        var application = require(Application.class);

        return storePath()
                .withChild(object.identifier().type().getName())
                .withChild(object.identifier().instance().identifier())
                .withRoot("/")
                .withSeparator(SEPARATOR);
    }

    /**
     * @return The settings type for the given node path
     */
    private Class<?> settingsType(StringPath path)
    {
        if (path.size() >= 2)
        {
            var typeName = path.get(path.size() - 2);
            var type = Classes.forName(typeName);
            if (type != null)
            {
                return type;
            }
        }
        return fail("Cannot get type from path: $", path);
    }

    /**
     * @return The path to the settings for this store in Zookeeper:
     *
     * <ol>
     *     <li>kivakit</li>
     *     <li>kivakit-version</li>
     *     <li>user-name</li>
     *     <li>application-name</li>
     *     <li>application-version</li>
     * </ol>
     * <p>
     * For example:
     * <pre>
     * /kivakit/1.1.3/jonathan/demo/1.0</pre>
     */
    private StringPath storePath()
    {
        var application = require(Application.class);

        return StringPath.stringPath(
                "kivakit",
                String.valueOf(KivaKit.get().kivakitVersion()),
                JavaVirtualMachine.property("user.name"),
                application.name(),
                application.version().toString());
    }
}

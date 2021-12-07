package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.BaseSettingsStore;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.configuration.settings.SettingsStore;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.types.Classes;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.serialization.json.GsonFactory;
import org.apache.zookeeper.CreateMode;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.SAVE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.UNLOAD;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * A {@link SettingsStore} that uses Apache Zookeeper to load and save settings objects so they can be easily accessed
 * in a clustered environment.
 *
 * <p><b>Creating a Settings Store</b></p>
 *
 * <p>
 * A {@link ZookeeperSettingsStore} can be constructed with or without an explicit {@link CreateMode}. If no create mode
 * is specified for the store, {@link ZookeeperConnection#defaultCreateMode()} will be used. The connection to Zookeeper
 * is maintained by a {@link ZookeeperConnection} that is configured with {@link ZookeeperConnection.Settings}. When
 * using the kivakit <i>-deployment</i> switch, the
 * <i>ZookeeperConnection.properties</i> file in the <i>deployments</i> package next to your application should look
 * like this:
 * </p>
 *
 * <pre>
 * class               = com.telenav.kivakit.settings.stores.zookeeper.ZookeeperConnection$Settings
 *
 * ports               = 127.0.0.1:2181,127.0.0.1:2182
 * timeout             = 60s
 * default-create-mode = PERSISTENT</pre>
 *
 * <p>
 * The comma-separated list of ports (host and port number) is used by the Zookeeper client when connecting to the
 * cluster.
 * </p>
 *
 * <p><b>Reacting to Changes</b></p>
 *
 * <p>
 * Because KivaKit's {@link Registry} methods pull objects from the registry, it is generally not necessary to respond
 * to Zookeeper data changes. When a node in Zookeeper that is storing a settings object changes, this settings store
 * will be updated with the new object and changes will be propagated to the global settings registry. The next time the
 * settings object is looked up, the new version will be retrieved.
 * </p>
 *
 * <p>
 * In less common cases, these methods can be overridden:
 * </p>
 * <ul>
 *     <li>{@link #onSettingsUpdated(StringPath, SettingsObject)} - Called when a settings object is created or updated</li>
 *     <li>{@link #onSettingsDeleted(StringPath, SettingsObject)} - Called when a settings object is deleted</li>
 * </ul>
 *
 * </p>
 *
 * @author jonathanl (shibo)
 * @see ZookeeperConnection
 * @see SettingsObject
 * @see InstanceIdentifier
 * @see StringPath
 */
public class ZookeeperSettingsStore extends BaseSettingsStore implements RegistryTrait, ZookeeperChangeListener
{
    /**
     * Path separator used when storing ephemeral nodes in Zookeeper.
     *
     * <p><b>NOTE</b></p>
     * <p>
     * This is a workaround for a Zookeeper limitation, namely that {@link CreateMode#EPHEMERAL} nodes cannot have
     * paths. So, it is necessary to flatten ephemeral paths into a single node name in the root: the path <i>/a/b/c</i>
     * becomes <i>/a::b::c</i>
     * </p>
     */
    private static final String EPHEMERAL_NODE_SEPARATOR = "::";

    /** Create mode for settings in this store */
    private CreateMode createMode;

    /** Connection to Zookeeper */
    private final ZookeeperConnection connection = new ZookeeperConnection();

    /**
     * Creates a settings store with the given explicit {@link CreateMode}. This overrides any setting in {@link
     * ZookeeperConnection.Settings}.
     *
     * @param createMode The explicit create mode to use for nodes in this store
     */
    public ZookeeperSettingsStore(CreateMode createMode)
    {
        this.createMode = createMode;
        connection.addChangeListener(this);
    }

    /**
     * Creates a settings store that uses the default {@link CreateMode} from {@link ZookeeperConnection.Settings}
     */
    public ZookeeperSettingsStore()
    {
        connection.addChangeListener(this);
    }

    /**
     * This settings store supports all access modes
     */
    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD, SAVE);
    }

    /**
     * <p>
     * Called when a node is created or its data changes.
     *
     * @param path The path to the node that changed
     */
    @Override
    public final void onNodeDataChanged(StringPath path)
    {
        readSettings(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onNodeDeleted(StringPath path)
    {
        // Get the settings object type and instance referred to by the given path
        var type = settingsType(path);
        var instance = instance(path);

        // If we can find the object,
        var object = lookup(type, instance);
        if (object != null)
        {
            // un-index it in the local store,
            var settings = new SettingsObject(object, type, instance);
            unindex(settings);

            // propagate the change,
            var update = propagateChangesTo();
            if (update != null)
            {
                update.unindex(settings);
            }

            // and notify the subclass of the deletion.
            onSettingsDeleted(unflatten(path), settings);
        }
        else
        {
            warning("No object registered for: $:$", type, instance);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MustBeInvokedByOverriders
    protected boolean onDelete(SettingsObject settings)
    {
        return connection.delete(path(settings));
    }

    /**
     * Deserializes the given data into an instance of the given type. By default uses the registered {@link
     * GsonFactory} to deserialize the data from JSON format.
     *
     * @param data The data to deserialize
     * @param type The type of object to deserialize
     */
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

        if (isEphemeral())
        {
            // Go through the child paths of the root node,
            var storePrefix = flatten(storePath()).join();
            for (var child : connection.children(root()))
            {
                // and if the child has data for this settings store,
                if (child.startsWith(storePrefix))
                {
                    // then read and add the settings
                    var path = StringPath.stringPath(child).withRoot("/");
                    settings.addIfNotNull(readSettings(path));
                }
            }
        }
        else
        {
            settings.addAll(loadRecursively(StringPath.stringPath("PERSISTENT")));
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
            connection.watch(path);
            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to save object: $", settings);
            return false;
        }
    }

    /**
     * Serializes the given object. By default, uses the registered{@link GsonFactory} to serialize to JSON format.
     */
    protected byte[] onSerialize(Object object)
    {
        var gson = require(GsonFactory.class).gson();
        var json = gson.toJson(object);
        return json.getBytes(UTF_8);
    }

    /**
     * Called when settings are deleted from Zookeeper
     *
     * @param path The path to the settings object in Zookeeper
     * @param settings The settings object
     */
    protected void onSettingsDeleted(StringPath path, SettingsObject settings)
    {
    }

    /**
     * Called when settings objects are updated in Zookeeper
     *
     * @param path The path to the settings object in Zookeeper
     * @param settings The settings object
     */
    protected void onSettingsUpdated(StringPath path, SettingsObject settings)
    {
    }

    /**
     * @return The given ephemeral path un-flattened using the separator for ephemeral nodes. For example, the ephemeral
     * node path /a::b::c becomes the hierarchical node path /a/b/c.
     */
    @NotNull
    protected StringPath unflatten(final StringPath path)
    {
        ensure(path.size() == 1);
        return StringPath.stringPath(StringPath.stringPath(StringList.split(path.get(0), EPHEMERAL_NODE_SEPARATOR)).asJavaPath());
    }

    /**
     * @return True if the given Zookeeper path was created
     */
    private boolean create(StringPath path)
    {
        var created = connection.create(path, OPEN_ACL_UNSAFE, createMode());
        connection.watch(path);
        return created;
    }

    /**
     * @return The create mode for paths in this store
     * @see CreateMode
     */
    private CreateMode createMode()
    {
        if (createMode == null)
        {
            createMode = connection.defaultCreateMode();
        }
        return createMode;
    }

    /**
     * @return The given path flattened using the separator for ephemeral nodes. For example, the hierarchical node path
     * /a/b/c becomes the flattened node path /a::b::c.
     */
    @NotNull
    private StringPath flatten(final StringPath path)
    {
        return StringPath.stringPath(path.join(EPHEMERAL_NODE_SEPARATOR));
    }

    /**
     * @return The {@link InstanceIdentifier} from the last element of the given node path
     * @see #path(SettingsObject)
     */
    private InstanceIdentifier instance(StringPath path)
    {
        return InstanceIdentifier.of(path.last());
    }

    /**
     * @return True if this is an ephemeral settings store
     */
    private boolean isEphemeral()
    {
        return createMode() == CreateMode.EPHEMERAL;
    }

    /**
     * Recursively load all child nodes of the given path
     *
     * @param path The path to load
     * @return The set of settings objects under the given path
     */
    private ObjectSet<SettingsObject> loadRecursively(final StringPath path)
    {
        var settings = new ObjectSet<SettingsObject>();

        // Add the data at this path
        if (!path.isEmpty())
        {
            settings.addIfNotNull(readSettings(path));
            connection.watch(path);
        }

        // Go through each child node of the given path
        for (var child : connection.children(path).prefixedWith("/"))
        {
            // and add the settings of that child
            var childPath = path.withChild(child);
            settings.addAll(loadRecursively(childPath));
            connection.watch(childPath);
        }

        return settings;
    }

    /**
     * @return The given path flattened or not based on whether or not this is an ephemeral store
     */
    private StringPath maybeFlatten(StringPath path)
    {
        // If the path is ephemeral,
        if (isEphemeral())
        {
            // we must flatten the path by joining it with a separator because Zookeeper doesn't
            // support children on ephemeral nodes.
            return flatten(path);
        }
        else
        {
            // otherwise, we use the full path
            return path;
        }
    }

    /**
     * Composes the Zookeeper path to the given settings object:
     *
     * <ol>
     *     <li>store-path</li>
     *     <li>type-name</li>
     *     <li>instance-identifier</li>
     * </ol>
     *
     * <p>
     * Paths to ephemeral nodes will be flattened to a single node in the root.
     * For example:
     * </p>
     *
     * <pre>/PERSISTENT/kivakit/1.0.3/jonathanl/demo/1.0/demo.Demo/SINGLETON</pre>
     * <pre>/EPHEMERAL/kivakit::1.0.3::jonathanl::demo::1.0::demo.Demo::SINGLETON</pre>
     *
     * @return A path to the given settings object
     * @see #storePath()
     */
    private StringPath path(SettingsObject object)
    {
        return maybeFlatten(storePath()
                .withChild(object.identifier().type().getName())
                .withChild(object.identifier().instance().identifier())
                .withRoot("/"));
    }

    /**
     * <ol>
     *     <li>Reads the settings object at the given path from Zookeeper</li>
     *     <li>Indexes the object</li>
     *     <li>Propagates changes to any dependent registry</li>
     *     <li>Calls {@link #onSettingsUpdated(StringPath, SettingsObject)}</li>
     *     <li>Adds a watcher for the given path</li>
     * </ol>
     *
     * @return The settings object at the given path
     */
    private SettingsObject readSettings(final StringPath path)
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
                    onSettingsUpdated(unflatten(path), settings);
                    connection.watch(path);
                    return settings;
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

        return null;
    }

    /**
     * @return The root path
     */
    private StringPath root()
    {
        return StringPath.stringPath(List.of()).withRoot("/");
    }

    /**
     * @return The settings type for the given node path
     */
    private Class<?> settingsType(StringPath path)
    {
        if (isEphemeral())
        {
            path = unflatten(path);
        }

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
     * Returns the path to the settings objects in this settings store in Zookeeper. The path is formed from these
     * elements:
     *
     * <ol>
     *     <li>[create-mode]</li>
     *     <li>kivakit</li>
     *     <li>[kivakit-version]</li>
     *     <li>[user-name]</li>
     *     <li>[application-name]</li>
     *     <li>[application-version]</li>
     * </ol>
     *
     * <p>
     * For example:
     * </p>
     *
     * <pre>/PERSISTENT/kivakit/1.0.3/jonathan/demo/1.0</pre>
     *
     * @return The path to the settings for this store in Zookeeper
     */
    private StringPath storePath()
    {
        var application = require(Application.class);

        return StringPath.stringPath(
                createMode().name(),
                "kivakit",
                String.valueOf(KivaKit.get().kivakitVersion()),
                JavaVirtualMachine.property("user.name"),
                application.name(),
                application.version().toString());
    }
}

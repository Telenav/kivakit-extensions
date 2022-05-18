package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.vm.Properties;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.OutputResource;
import com.telenav.kivakit.resource.serialization.ObjectMetadata;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.settings.BaseSettingsStore;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;
import kivakit.merged.zookeeper.CreateMode;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import static com.telenav.kivakit.core.project.Project.resolveProject;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.SAVE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;
import static kivakit.merged.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

/**
 * A {@link SettingsStore} that uses Apache Zookeeper to load and save settings objects, so they can be easily accessed
 * in a clustered environment.
 *
 * <p><b>Creating a Zookeeper Settings Store</b></p>
 *
 * <p>
 * A {@link ZookeeperSettingsStore} can be constructed with or without an explicit Zookeeper {@link CreateMode}. The
 * connection to Zookeeper is maintained by a {@link ZookeeperConnection} that is configured with {@link
 * ZookeeperConnection.Settings}. If no explicit create mode is specified for the store, the default create mode
 * specified by {@link ZookeeperConnection.Settings} will be used.
 * </p>
 *
 * <p><b>Configuration</b></p>
 * <p>
 * When using the kivakit <i>-deployment</i> switch, the <i>ZookeeperConnection.properties</i> file in the
 * <i>deployments</i> package next to your application should look similar to this (but will vary depending
 * on your Zookeeper installation):
 * </p>
 *
 * <pre>
 * class   = com.telenav.kivakit.settings.stores.zookeeper.ZookeeperConnection$Settings
 *
 * ports   = 127.0.0.1:2181,127.0.0.1:2182
 * timeout = 60s
 * create-mode = PERSISTENT</pre>
 *
 * <p>
 * The comma-separated list of ports (host and port number) is used by the Zookeeper client when connecting to the
 * cluster.
 * </p>
 *
 * <p><b>Loading Settings</b></p>
 *
 * <p>
 * ZookeeperSettingsStore extends {@link BaseSettingsStore}, which implements {@link SettingsStore}. So, it can be used
 * in the same way as other KivaKit settings stores. The settings for an application can be loaded from the Zookeeper
 * settings store with this line of code:
 * </p>
 *
 * <pre>    registerSettingsIn(listenTo(new ZookeeperSettingsStore()));</pre>
 *
 * <p><b>Initializing Settings</b></p>
 *
 * <p>
 * If the desired settings are not available when the {@link ZookeeperSettingsStore} has been loaded, it may be
 * necessary to save some initial settings (it is also possible to store the JSON in Zookeeper directly). Settings can
 * be saved to {@link ZookeeperSettingsStore} using the {@link ZookeeperSettingsStore#save(SettingsObject)} method. The
 * code to load-or-initialize settings looks like:
 * </p>
 *
 * <pre>
 * // Create the Zookeeper settings store,
 * var store = listenTo(register(new ZookeeperSettingsStore()));
 *
 * // load settings from the store,
 * registerSettingsIn(store);
 *
 * // and if our required settings class is absent,
 * if (!hasSettings(MySettings.class))
 * {
 *     // save a default object to Zookeeper,
 *     store.save(new MySettings());
 *
 *     // and re-load the store.
 *     registerSettingsIn(store);
 * }</pre>
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
 * @see BaseSettingsStore
 * @see SettingsStore
 */
public class ZookeeperSettingsStore extends BaseSettingsStore implements
        ZookeeperChangeListener,
        ZookeeperConnectionListener
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

    /** Connection to Zookeeper */
    private final ZookeeperConnection connection = listenTo(register(new ZookeeperConnection(this, this)));

    /** Create mode for settings in this store */
    private CreateMode createMode;

    /** The serializer for saving and loading objects in the store */
    private final ObjectSerializer serializer;

    /**
     * Creates a settings store with the given explicit {@link CreateMode}. This overrides any setting in {@link
     * ZookeeperConnection.Settings}.
     *
     * @param createMode The explicit create mode to use for nodes in this store
     */
    public ZookeeperSettingsStore(CreateMode createMode, ObjectSerializer serializer)
    {
        this.createMode = createMode;
        this.serializer = serializer;
    }

    /**
     * Creates a settings store that uses the default {@link CreateMode} from {@link ZookeeperConnection.Settings}
     */
    public ZookeeperSettingsStore(ObjectSerializer serializer)
    {
        this.serializer = serializer;
    }

    /**
     * This store supports all access modes
     */
    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD, SAVE);
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
    public SettingsObject loadSettings(StringPath path)
    {
        trace("Loading settings: $", path);

        // Get the settings object type and instance referred to by the given path
        var type = settingsType(path);
        if (type != null)
        {
            var instance = instance(path);

            try
            {
                // read the altered settings object data,
                var data = connection.read(path);
                if (data != null)
                {
                    // deserialize the object,
                    var object = onDeserialize(data, type);
                    if (object != null)
                    {
                        // then index it,
                        var settings = new SettingsObject(object, type, instance);
                        index(settings);

                        // propagate the change,
                        var update = propagateChangesTo();
                        if (update != null)
                        {
                            update.index(settings);
                        }

                        // and tell the subclass we updated.
                        trace("Load settings: $", path);
                        onSettingsUpdated(unflatten(path), settings);

                        return settings;
                    }
                    else
                    {
                        warning("Could not deserialize: $: $", path, new String(data));
                    }
                }
                else
                {
                    warning("No data read from: $", path);
                }
            }
            catch (Exception e)
            {
                problem(e, "Could not update: $", path);
            }
        }
        else
        {
            warning("Could not parse class from: $", path);
        }

        return null;
    }

    /**
     * Reload the settings store if children changed
     *
     * @param path The path to the node
     */
    @Override
    public final void onNodeChildrenChanged(StringPath path)
    {
        trace("onNodeChildrenChanged($)", path);
        reload();

        connection.watchRoot();
    }

    @Override
    public final void onNodeCreated(StringPath path)
    {
        trace("onNodeCreated($)", path);
        reload();

        connection.watchRoot();
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
        trace("onNodeDataChanged($)", path);
        loadSettings(path);

        connection.watch(path);
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
            trace("Settings deleted: $", path);
        }
        else
        {
            warning("No object registered for: $:$", type, instance);
        }
    }

    /**
     * @return The root path of this settings store
     */
    public StringPath root()
    {
        return connection.root();
    }

    /**
     * @return The given ephemeral path un-flattened using the separator for ephemeral nodes. For example, the ephemeral
     * node path /a::b::c becomes the hierarchical node path /a/b/c.
     */
    @NotNull
    public StringPath unflatten(StringPath path)
    {
        if (path.size() == 1)
        {
            return StringPath.stringPath(StringPath.stringPath(StringList.split(path.get(0), EPHEMERAL_NODE_SEPARATOR)).asJavaPath()).withRoot("/");
        }

        return path;
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
     * Deserializes the given data into an instance of the given type using the {@link ObjectSerializer} passed to the
     * constructor.
     *
     * @param data The data to deserialize
     * @param type The type of object to deserialize
     */
    protected <T> T onDeserialize(byte[] data, Class<T> type)
    {
        var input = new InputResource(new ByteArrayInputStream(data));
        return serializer.read(input, type, ObjectMetadata.TYPE).object();
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
            for (var child : connection.children(connection.root()))
            {
                // and if the child has data for this settings store,
                if (child.startsWith(storePrefix))
                {
                    // then read and add the settings,
                    var path = StringPath.stringPath(child).withRoot("/");
                    settings.addIfNotNull(loadSettings(path));

                    // and watch the path for changes
                    connection.watch(path);
                }
            }
        }
        else
        {
            settings.addAll(loadRecursively(StringPath.stringPath("PERSISTENT").withRoot("/")));
        }

        connection.watchRoot();

        showWatchers();

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
            // Serialize the object,
            var data = onSerialize(settings.object());

            // write the serialized data to Zookeeper, triggering an update notification,
            var created = connection.create(path, data, OPEN_ACL_UNSAFE, createMode());

            // and watch the new node in case it changes.
            connection.watch(created);

            return true;
        }
        catch (Exception e)
        {
            problem(e, "Unable to save object: $", settings);
            return false;
        }
    }

    /**
     * Serializes the given object using the {@link ObjectSerializer} passed to the constructor.
     */
    protected byte[] onSerialize(Object object)
    {
        var bytes = new ByteArrayOutputStream();
        var output = new OutputResource(bytes);
        serializer.write(output, new SerializableObject<>(object));
        return bytes.toByteArray();
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
     * Called when settings objects are initially loaded or updated
     *
     * @param path The path to the settings object in Zookeeper
     * @param settings The settings object
     */
    protected void onSettingsUpdated(StringPath path, SettingsObject settings)
    {
    }

    /**
     * @return Create mode for paths in this store
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
    private StringPath flatten(StringPath path)
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
        return createMode() == CreateMode.EPHEMERAL
                || createMode() == CreateMode.EPHEMERAL_SEQUENTIAL;
    }

    /**
     * Recursively load all child nodes of the given path
     *
     * @param path The path to load
     * @return The set of settings objects under the given path
     */
    private ObjectSet<SettingsObject> loadRecursively(StringPath path)
    {
        var settings = new ObjectSet<SettingsObject>();

        // Add the data at this path
        if (path.size() > 1)
        {
            settings.addIfNotNull(loadSettings(path));
        }

        // Go through each child node of the given path
        for (var child : connection.children(path))
        {
            // and add the settings of that child
            settings.addAll(loadRecursively(path.withChild(child)));
        }

        // Watch this path for changes
        connection.watch(path);

        return settings;
    }

    /**
     * @return The given path flattened or not based on whether this is an ephemeral store
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
            return Classes.forName(typeName);
        }

        return null;
    }

    /**
     * Shows the watchers for this zookeeper settings store
     */
    private void showWatchers()
    {
        if (isDebugOn())
        {
            var list = new StringList();
            for (var at : connection.watchers().keySet())
            {
                list.add(unflatten(at).asContraction(100));
            }

            trace(list.titledBox("Watches"));
        }
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
                String.valueOf(resolveProject(KivaKit.class).kivakitVersion()),
                Properties.property("user.name"),
                application.name(),
                application.version().toString());
    }
}

package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.path.StringPath;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface ZookeeperChangeListener
{
    /**
     * Called when the children of a Zookeeper node have changed
     *
     * @param path The path to the node
     */
    default void onNodeChildrenChanged(StringPath path)
    {
    }

    /**
     * Called when a Zookeeper node is created
     *
     * @param path The path to the node
     */
    default void onNodeCreated(StringPath path)
    {
    }

    /**
     * Called when the data for a Zookeeper node has changed
     *
     * @param path The path to the node
     */
    default void onNodeDataChanged(StringPath path)
    {
    }

    /**
     * Called when a Zookeeper node is deleted
     *
     * @param path The path to the node
     */
    default void onNodeDeleted(StringPath path)
    {
    }
}

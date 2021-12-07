package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.kernel.language.paths.StringPath;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
public interface ZookeeperChangeListener
{
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
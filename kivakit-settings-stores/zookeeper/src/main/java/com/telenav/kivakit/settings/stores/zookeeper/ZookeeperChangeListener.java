package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.kernel.language.paths.StringPath;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
public interface ZookeeperChangeListener
{
    default void onNodeCreated(StringPath path)
    {
    }

    default void onNodeDataChanged(StringPath path)
    {
    }

    default void onNodeDeleted(StringPath path)
    {
    }
}
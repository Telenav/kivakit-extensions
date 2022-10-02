package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.path.StringPath;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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

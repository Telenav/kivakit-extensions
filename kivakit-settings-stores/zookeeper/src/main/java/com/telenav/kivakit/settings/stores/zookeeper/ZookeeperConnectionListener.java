package com.telenav.kivakit.settings.stores.zookeeper;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
public interface ZookeeperConnectionListener
{
    default void onConnected()
    {
    }

    default void onDisconnected()
    {
    }
}
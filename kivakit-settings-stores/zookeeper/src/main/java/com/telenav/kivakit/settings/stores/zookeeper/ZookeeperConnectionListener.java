package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface ZookeeperConnectionListener
{
    default void onConnected()
    {
    }

    default void onDisconnected()
    {
    }
}

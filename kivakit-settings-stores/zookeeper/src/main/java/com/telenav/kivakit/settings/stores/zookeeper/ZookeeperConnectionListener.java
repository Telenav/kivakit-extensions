package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Methods that are called when Zookeeper data is created, changes or is deleted.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public interface ZookeeperConnectionListener
{
    default void onConnected()
    {
    }

    default void onDisconnected()
    {
    }
}

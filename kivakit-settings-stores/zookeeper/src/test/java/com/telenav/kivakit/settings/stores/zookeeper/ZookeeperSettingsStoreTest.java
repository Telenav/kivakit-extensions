package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ZookeeperSettingsStoreTest extends UnitTest implements ComponentMixin
{
    public static class Settings
    {
        int x;

        public Settings(final int x)
        {
            this.x = x;
        }
    }

    @Test
    public void test()
    {
        // Register zookeeper settings,
        register(new ZookeeperSettingsStore.Settings());

        // create store,
        var store = register(new ZookeeperSettingsStore());

        // save settings to store
        saveSettingsTo(store, new Settings(7));

        // clear settings,
        clearSettings();

        // then reload them,
        registerSettingsIn(store);

        // and check the result.
        var reloaded = requireSettings(Settings.class);
        ensureEqual(reloaded.x, 7);
    }
}

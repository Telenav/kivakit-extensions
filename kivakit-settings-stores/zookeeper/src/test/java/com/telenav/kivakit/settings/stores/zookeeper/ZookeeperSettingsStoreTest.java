package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.configuration.settings.stores.resource.PackageSettingsStore;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ZookeeperSettingsStoreTest extends UnitTest implements ComponentMixin
{
    public static class Settings
    {
        int x;

        public Settings(int x)
        {
            this.x = x;
        }
    }

    @Test
    public void test()
    {
        // Register zookeeper settings,
        registerSettingsIn(PackageSettingsStore.of(this, PackagePath.packagePath(getClass())));

        // create store,
        var store = listenTo(register(new ZookeeperSettingsStore()));

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

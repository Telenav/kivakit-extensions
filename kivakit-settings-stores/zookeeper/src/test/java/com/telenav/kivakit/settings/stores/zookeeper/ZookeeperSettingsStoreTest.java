package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.resource.serialization.serializers.JsonSerializer;
import com.telenav.kivakit.settings.stores.PackageSettingsStore;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.zookeeper.CreateMode.PERSISTENT;

/**
 * This test can only be used if zookeeper is running on the local host on port 2181, so it is ignored by default
 */
@Ignore
public class ZookeeperSettingsStoreTest extends UnitTest implements ComponentMixin
{
    public static class Settings
    {
        @KivaKitIncludeProperty
        int x;

        public Settings()
        {
        }

        public Settings(int x)
        {
            this.x = x;
        }
    }

    @Test
    public void test()
    {
        // Register zookeeper settings,
        registerSettingsIn(listenTo(PackageSettingsStore.of(this, packagePath())));

        // create zookeeper settings store,
        var store = listenTo(register(new ZookeeperSettingsStore(PERSISTENT, new JsonSerializer())));

        // register a dummy application because settings are saved under application name and version,
        register(new Application()
        {
            @Override
            public String name()
            {
                return ZookeeperSettingsStoreTest.class.getSimpleName();
            }

            @Override
            protected void onRun()
            {
            }
        });

        // save settings to store with x = 7,
        saveSettingsTo(store, new Settings(7));

        // clear in-memory settings index,
        store.unload();

        // then reload them from zookeeper,
        registerSettingsIn(store);

        // and check the result.
        var reloaded = requireSettings(Settings.class);
        ensureEqual(reloaded.x, 7);
    }
}

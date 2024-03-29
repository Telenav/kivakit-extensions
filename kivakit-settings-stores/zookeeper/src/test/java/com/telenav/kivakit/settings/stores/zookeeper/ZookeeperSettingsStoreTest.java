package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import com.telenav.kivakit.serialization.properties.PropertiesObjectSerializer;
import com.telenav.kivakit.settings.SettingsTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.resource.Extension.JSON;
import static com.telenav.kivakit.resource.Extension.PROPERTIES;
import static com.telenav.third.party.zookeeper.CreateMode.PERSISTENT;

/**
 * This test can only be used if zookeeper is running on the local host on port 2181, so it is ignored by default
 */
@Ignore
public class ZookeeperSettingsStoreTest extends UnitTest implements ComponentMixin, SettingsTrait
{
    @SuppressWarnings("unused")
    public static class Settings
    {
        @IncludeProperty
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
        register(new KivaKitCoreGsonFactory());

        var serializers = new ObjectSerializerRegistry();
        serializers.add(JSON, listenTo(new GsonObjectSerializer()));
        serializers.add(PROPERTIES, listenTo(new PropertiesObjectSerializer()));
        register(serializers);

        // Register zookeeper settings,
        registerSettingsIn(packageForThis());

        // create zookeeper settings store,
        var store = listenTo(register(new ZookeeperSettingsStore(PERSISTENT, new GsonObjectSerializer())));

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
        saveSettings(store, new Settings(7));

        // clear in-memory settings index,
        store.clearRegistry();

        // then reload them from zookeeper,
        registerSettingsIn(store);

        // and check the result.
        var reloaded = requireSettings(Settings.class);
        ensureEqual(reloaded.x, 7);
    }
}

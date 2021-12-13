package com.telenav.kivakit.settings.stores.zookeeper;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.google.gson.annotations.Expose;
import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.configuration.settings.stores.resource.PackageSettingsStore;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.serialization.json.DefaultGsonFactory;
import com.telenav.kivakit.test.UnitTest;
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
        @Tag(1)
        @Expose
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
        var store = listenTo(register(new ZookeeperSettingsStore(PERSISTENT)));

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

        // register a Gson factory,
        register(new DefaultGsonFactory(this)
                .withPrettyPrinting(true)
                .withVersion(Version.parse(this, "1.0"))
                .withRequireExposeAnnotation(false));

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

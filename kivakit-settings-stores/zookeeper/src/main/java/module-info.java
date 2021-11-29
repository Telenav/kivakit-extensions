open module kivakit.settings.stores.zookeeper
{
    // KivaKit
    requires kivakit.configuration;
    requires kivakit.test;

    // Zookeeper
    requires transitive kivakit.merged.zookeeper;
    requires kivakit.network.core;
    requires kivakit.component;
    requires kivakit.application;

    // Module exports
    exports com.telenav.kivakit.settings.stores.zookeeper;
}

open module kivakit.settings.stores.zookeeper
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.configuration;
    requires transitive kivakit.network.core;
    requires transitive kivakit.component;
    requires kivakit.test;

    // Zookeeper
    requires transitive kivakit.merged.zookeeper;

    // Module exports
    exports com.telenav.kivakit.settings.stores.zookeeper;
}

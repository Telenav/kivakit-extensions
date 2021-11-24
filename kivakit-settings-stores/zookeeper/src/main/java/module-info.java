open module kivakit.settings.stores.zookeeper
{
    // KivaKit
    requires kivakit.configuration;
    requires kivakit.test;

    // Zookeeper
    requires transitive zookeeper;
    requires transitive zookeeper.jute;
    requires kivakit.network.core;
    requires kivakit.component;
    requires kivakit.application;

    // Module exports
    exports com.telenav.kivakit.settings.stores.zookeeper;
}

open module kivakit.settings.stores.zookeeper
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.settings;
    requires transitive kivakit.network.core;
    requires transitive kivakit.component;

    // Zookeeper
    requires transitive kivakit.merged.zookeeper;

    // Module exports
    exports com.telenav.kivakit.settings.stores.zookeeper;
}

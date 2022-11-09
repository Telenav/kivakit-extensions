open module kivakit.settings.stores.zookeeper
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.settings;
    requires transitive kivakit.network.core;
    requires transitive kivakit.component;
    requires transitive kivakit.conversion;

    // Zookeeper
    requires transitive telenav.third.party.zookeeper;

    // Module exports
    exports com.telenav.kivakit.settings.stores.zookeeper;
}

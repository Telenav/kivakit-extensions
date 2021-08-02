open module kivakit.service.viewer
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.network.http;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.service.client;

    // Module exports
    exports com.telenav.kivakit.service.viewer;
}

open module mesakit.tools.applications.log.viewer
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.network.core;
    requires transitive kivakit.logs.client;
    requires transitive kivakit.logs.server;
    requires transitive kivakit.ui.desktop;

    // Module exports
    exports com.telenav.kivakit.logs.viewer;
}

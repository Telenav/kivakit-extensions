open module mesakit.tools.applications.log.viewer
{
    // KivaKit
    requires kivakit.application;
    requires kivakit.network.core;
    requires kivakit.logs.client;
    requires kivakit.logs.server;
    requires kivakit.ui.desktop;

    // Module exports
    exports com.telenav.kivakit.logs.viewer;
}

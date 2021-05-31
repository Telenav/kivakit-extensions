open module mesakit.tools.applications.log.viewer
{
    requires transitive kivakit.application;
    requires transitive kivakit.network.core;
    requires transitive kivakit.logs.client;
    requires transitive kivakit.logs.server;
    requires transitive kivakit.ui.desktop;

    exports com.telenav.kivakit.logs.viewer;
}

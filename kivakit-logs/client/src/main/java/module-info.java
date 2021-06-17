open module kivakit.logs.client
{
    requires java.desktop;
    requires java.prefs;

    requires transitive kivakit.ui.desktop;
    requires transitive kivakit.logs.server;
    requires transitive kivakit.primitive.collections;
    requires transitive kivakit.service.client;
    requires transitive kivakit.network.core;

    provides com.telenav.kivakit.kernel.logging.Log with
            com.telenav.kivakit.logs.client.ClientLog;

    exports com.telenav.kivakit.logs.client;
}

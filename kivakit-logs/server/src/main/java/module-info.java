open module kivakit.logs.server
{
    requires transitive kivakit.network.core;
    requires transitive kivakit.network.socket;
    requires transitive kivakit.service.registry;
    requires kivakit.service.client;

    provides com.telenav.kivakit.kernel.logging.Log
            with com.telenav.kivakit.logs.server.ServerLog;

    exports com.telenav.kivakit.logs.server;
    exports com.telenav.kivakit.logs.server.project;
    exports com.telenav.kivakit.logs.server.session;
}

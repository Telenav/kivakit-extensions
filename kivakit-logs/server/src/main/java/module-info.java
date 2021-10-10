import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.server.ServerLog;

open module kivakit.logs.server
{
    provides Log with ServerLog;

    // KivaKit
    requires transitive kivakit.network.socket;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.service.client;

    requires org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.logs.server;
    exports com.telenav.kivakit.logs.server.project;
    exports com.telenav.kivakit.logs.server.session;
}

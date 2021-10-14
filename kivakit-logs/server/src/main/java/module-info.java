import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.server.ServerLog;

open module kivakit.logs.server
{
    provides Log with ServerLog;

    // KivaKit
    requires kivakit.network.socket;
    requires kivakit.service.registry;
    requires kivakit.service.client;

    // Module exports
    exports com.telenav.kivakit.logs.server;
    exports com.telenav.kivakit.logs.server.project;
    exports com.telenav.kivakit.logs.server.session;
}

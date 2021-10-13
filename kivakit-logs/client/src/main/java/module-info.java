import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.client.ClientLog;

open module kivakit.logs.client
{
    provides Log with ClientLog;

    // KivaKit
    requires kivakit.ui.desktop;
    requires kivakit.logs.server;
    requires kivakit.primitive.collections;
    requires kivakit.service.client;
    requires kivakit.network.core;

    requires org.jetbrains.annotations;

    // Java
    requires java.desktop;
    requires java.prefs;

    // Module exports
    exports com.telenav.kivakit.logs.client;
}

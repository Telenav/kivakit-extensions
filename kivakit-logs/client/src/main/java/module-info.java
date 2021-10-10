import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.client.ClientLog;

open module kivakit.logs.client
{
    provides Log with ClientLog;

    // KivaKit
    requires transitive kivakit.ui.desktop;
    requires transitive kivakit.logs.server;
    requires transitive kivakit.primitive.collections;
    requires transitive kivakit.service.client;
    requires transitive kivakit.network.core;

    requires org.jetbrains.annotations;

    // Java
    requires java.desktop;
    requires java.prefs;

    // Module exports
    exports com.telenav.kivakit.logs.client;
}

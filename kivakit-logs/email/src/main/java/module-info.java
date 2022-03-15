import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.logs.email.EmailLog;

open module kivakit.logs.email
{
    provides Log with EmailLog;

    // KivaKit
    requires kivakit.network.email;

    // Module exports
    exports com.telenav.kivakit.logs.email;
    exports com.telenav.kivakit.logs.email.lexakai;
}

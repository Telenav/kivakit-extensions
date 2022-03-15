import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.logs.file.FileLog;

open module kivakit.logs.file
{
    provides Log with FileLog;

    // KivaKit
    requires transitive kivakit.resource;

    // Module exports
    exports com.telenav.kivakit.logs.file;
    exports com.telenav.kivakit.logs.file.lexakai;
}

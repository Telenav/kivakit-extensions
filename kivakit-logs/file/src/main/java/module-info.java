import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.file.FileLog;

open module kivakit.logs.file
{
    provides Log with FileLog;

    // KivaKit
    requires transitive kivakit.resource;

    // Module exports
    exports com.telenav.kivakit.logs.file;
    exports com.telenav.kivakit.logs.file.project.lexakai.diagrams;
}

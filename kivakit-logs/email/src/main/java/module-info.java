import com.telenav.kivakit.kernel.logging.Log;
import com.telenav.kivakit.logs.email.EmailLog;

open module kivakit.logs.email
{
    requires transitive kivakit.network.email;

    provides Log with EmailLog;

    exports com.telenav.kivakit.logs.email;
    exports com.telenav.kivakit.logs.email.project;
    exports com.telenav.kivakit.logs.email.project.lexakai.diagrams;
}

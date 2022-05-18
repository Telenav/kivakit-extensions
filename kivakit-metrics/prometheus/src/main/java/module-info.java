@SuppressWarnings("JavaRequiresAutoModule")
open module kivakit.metrics.prometheus
{
    // KivaKit
    requires transitive kivakit.metrics.core;
    requires transitive kivakit.web.jetty;
    requires kivakit.merged.prometheus;

    // Web
    requires org.eclipse.jetty.servlet;

    // Exports
    exports com.telenav.kivakit.metrics.prometheus;
}

open module kivakit.metrics.prometheus
{
    // KivaKit
    requires transitive kivakit.metrics.core;
    requires kivakit.merged.prometheus;
    requires transitive kivakit.web.jetty;
    requires org.eclipse.jetty.servlet;

    exports com.telenav.kivakit.metrics.prometheus;
}

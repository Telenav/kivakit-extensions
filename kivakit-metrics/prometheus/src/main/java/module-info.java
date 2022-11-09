@SuppressWarnings("requires-transitive-automatic")
open module kivakit.metrics.prometheus
{
    // KivaKit
    requires transitive kivakit.metrics.core;
    requires transitive kivakit.web.jetty;
    requires transitive telenav.third.party.zookeeper;

    // Web
    requires org.eclipse.jetty.servlet;

    // Exports
    exports com.telenav.kivakit.metrics.prometheus;
}

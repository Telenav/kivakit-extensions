open module kivakit.web.jetty
{
    // KivaKit
    requires transitive kivakit.component;
    requires transitive kivakit.network.http;

    // Jetty
    requires transitive org.eclipse.jetty.util;
    requires transitive org.eclipse.jetty.server;
    requires transitive org.eclipse.jetty.servlets;
    requires org.eclipse.jetty.servlet;

    // Module exports
    exports com.telenav.kivakit.web.jetty;
    exports com.telenav.kivakit.web.jetty.resources;
}

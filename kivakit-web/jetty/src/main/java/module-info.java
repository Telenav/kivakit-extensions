open module kivakit.web.jetty
{
    // KivaKit
    requires transitive kivakit.component;

    // Jetty
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    requires org.eclipse.jetty.util;
    requires org.eclipse.jetty.webapp;
    requires transitive javax.servlet.api;
    requires transitive kivakit.test;
    requires transitive kivakit.test.internal;
    requires kivakit.network.http;

    // Module exports
    exports com.telenav.kivakit.web.jetty;
    exports com.telenav.kivakit.web.jetty.resources;
}

open module kivakit.web.jetty
{
    // KivaKit
    requires transitive kivakit.component;
    requires transitive kivakit.network.http;
    requires kivakit.test;

    // Jetty
    requires transitive javax.servlet.api;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    requires org.eclipse.jetty.util;
    requires org.eclipse.jetty.webapp;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.web.jetty;
    exports com.telenav.kivakit.web.jetty.resources;
}

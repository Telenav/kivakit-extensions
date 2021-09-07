open module kivakit.web.jetty
{
    // KivaKit
    requires transitive kivakit.network.http;

    // Jetty
    requires transitive javax.servlet.api;
    requires transitive org.eclipse.jetty.server;
    requires transitive org.eclipse.jetty.servlet;
    requires transitive org.eclipse.jetty.util;
    requires transitive org.eclipse.jetty.webapp;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.web.jetty;
    exports com.telenav.kivakit.web.jetty.resources;
}

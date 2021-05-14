open module kivakit.web.jetty
{
    requires transitive kivakit.network.http;

    requires transitive javax.servlet.api;

    requires transitive org.eclipse.jetty.server;
    requires transitive org.eclipse.jetty.servlet;
    requires transitive org.eclipse.jetty.util;
    requires transitive org.eclipse.jetty.webapp;

    exports com.telenav.kivakit.web.jetty;
    exports com.telenav.kivakit.web.jetty.project;
    exports com.telenav.kivakit.web.jetty.resources;
}

open module kivakit.web.jersey
{
    // KivaKit
    requires transitive kivakit.component;
    requires transitive kivakit.web.jetty;

    // Jersey
    requires transitive jersey.common;
    requires transitive jersey.container.servlet.core;
    requires transitive jersey.server;

    // Java
    requires transitive java.ws.rs;
    requires transitive java.xml.bind;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.web.jersey;
    exports com.telenav.kivakit.web.jersey.project;
}

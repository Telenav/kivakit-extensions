open module kivakit.web.jersey
{
    // KivaKit
    requires transitive kivakit.web.jetty;

    // Java
    requires transitive java.ws.rs;
    requires java.xml.bind;
    requires transitive jakarta.activation;

    // Jersey
    requires jersey.common;
    requires jersey.container.servlet.core;
    requires jersey.server;
    requires jersey.hk2;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // XML
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires kivakit.serialization.gson;

    // Module exports
    exports com.telenav.kivakit.web.jersey;
}

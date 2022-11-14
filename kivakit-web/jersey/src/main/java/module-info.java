open module kivakit.web.jersey
{
    // KivaKit
    requires transitive kivakit.web.jetty;

    // Jersey
    requires jersey.common;
    requires jersey.container.servlet.core;
    requires jersey.server;
    requires jersey.hk2;

    // XML
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires kivakit.serialization.gson;
    requires jakarta.ws.rs;
    requires org.eclipse.jetty.servlet;

    // Module exports
    exports com.telenav.kivakit.web.jersey;
}

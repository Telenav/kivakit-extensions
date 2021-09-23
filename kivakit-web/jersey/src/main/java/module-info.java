open module kivakit.web.jersey
{
    // KivaKit
    requires transitive kivakit.component;
    requires transitive kivakit.serialization.json;
    requires transitive kivakit.web.jetty;

    // Java
    requires transitive java.xml.bind;
    requires transitive java.ws.rs;
    requires transitive jakarta.activation;

    // Jersey
    requires transitive jersey.common;
    requires transitive jersey.container.servlet.core;
    requires transitive jersey.server;

    // JSON
    requires transitive gson;

    // XML
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

    // Module exports
    exports com.telenav.kivakit.web.jersey;
}

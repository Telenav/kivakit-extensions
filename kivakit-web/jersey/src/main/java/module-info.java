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
    requires jersey.common;
    requires jersey.container.servlet.core;
    requires jersey.server;
    requires jersey.hk2;

    // JSON
    requires gson;

    // XML
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
    requires java.logging;

    // Module exports
    exports com.telenav.kivakit.web.jersey;
}

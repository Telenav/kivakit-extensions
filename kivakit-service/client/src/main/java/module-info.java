open module kivakit.service.client
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.network.http;

    // Jersey and XML binding
    requires transitive jersey.client;
    requires transitive jersey.media.json.jackson;
    requires transitive com.fasterxml.jackson.core;
    requires transitive java.ws.rs;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.service.registry.client;
    exports com.telenav.kivakit.service.registry.client.project.lexakai.diagrams;
}

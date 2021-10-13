open module kivakit.service.client
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.network.http;

    // Jersey and XML binding
    requires jersey.client;
    requires jersey.media.json.jackson;
    requires com.fasterxml.jackson.core;
    requires java.ws.rs;
    requires jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.service.registry.client;
    exports com.telenav.kivakit.service.registry.client.project.lexakai.diagrams;
}

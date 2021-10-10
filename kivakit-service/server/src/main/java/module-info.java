open module kivakit.service.server
{
    // KivaKit
    requires transitive kivakit.microservice;
    requires transitive kivakit.service.client;
    requires transitive kivakit.web.jetty;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;

    // Java
    requires transitive java.prefs;

    // Wicket
    requires wicket.extensions;
    requires wicket.jquery.ui;
    requires wicket.jquery.ui.core;
    requires wicket.util;

    // Serialization
    requires transitive org.danekja.jdk.serializable.functional;

    // Swagger annotations
    requires io.swagger.v3.oas.annotations;

    // Module exports
    exports com.telenav.kivakit.service.registry.server.project.lexakai.diagrams;
    exports com.telenav.kivakit.service.registry.server.rest;
    exports com.telenav.kivakit.service.registry.server.webapp.pages.home;
    exports com.telenav.kivakit.service.registry.server.webapp;
    exports com.telenav.kivakit.service.registry.server;
}

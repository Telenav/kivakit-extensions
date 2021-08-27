open module kivakit.service.server
{
    // KivaKit
    requires transitive kivakit.service.client;
    requires transitive kivakit.web.jetty;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;

    // Java
    requires transitive java.prefs;

    // Wicket
    requires transitive wicket.extensions;
    requires transitive wicket.jquery.ui;
    requires transitive wicket.jquery.ui.core;
    requires transitive wicket.util;

    // Serialization
    requires transitive org.danekja.jdk.serializable.functional;

    // Swagger annotations
    requires transitive io.swagger.v3.oas.annotations;

    // Module exports
    exports com.telenav.kivakit.service.registry.server.project;
    exports com.telenav.kivakit.service.registry.server.project.lexakai.diagrams;
    exports com.telenav.kivakit.service.registry.server.rest;
    exports com.telenav.kivakit.service.registry.server.webapp.pages.home;
    exports com.telenav.kivakit.service.registry.server.webapp;
}

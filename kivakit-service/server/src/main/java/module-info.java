open module kivakit.service.server
{
    // KivaKit
    requires kivakit.microservice;
    requires kivakit.service.client;
    requires kivakit.web.jetty;
    requires kivakit.web.wicket;
    requires kivakit.web.jersey;
    requires kivakit.web.swagger;

    // Java
    requires java.prefs;

    // Wicket
    requires wicket.jquery.ui;
    requires wicket.jquery.ui.core;

    // Serialization
    requires org.danekja.jdk.serializable.functional;

    // Swagger annotations
    requires io.swagger.v3.oas.annotations;

    // Module exports
    exports com.telenav.kivakit.service.registry.server.project.lexakai.diagrams;
    exports com.telenav.kivakit.service.registry.server.rest;
    exports com.telenav.kivakit.service.registry.server.webapp.pages.home;
    exports com.telenav.kivakit.service.registry.server.webapp;
    exports com.telenav.kivakit.service.registry.server;
}

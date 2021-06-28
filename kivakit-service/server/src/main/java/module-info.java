open module kivakit.service.server
{
    requires transitive kivakit.service.client;

    requires transitive kivakit.web.jetty;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;

    requires wicket.extensions;
    requires wicket.jquery.ui;
    requires wicket.jquery.ui.core;
    requires wicket.util;

    requires org.danekja.jdk.serializable.functional;
    requires java.prefs;

    requires io.swagger.v3.oas.annotations;

    exports com.telenav.kivakit.service.registry.server.project;
    exports com.telenav.kivakit.service.registry.server.project.lexakai.diagrams;
    exports com.telenav.kivakit.service.registry.server.rest;
    exports com.telenav.kivakit.service.registry.server.webapp.pages.home;
    exports com.telenav.kivakit.service.registry.server.webapp;
}

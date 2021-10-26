open module kivakit.web.swagger
{
    // KivaKit
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.resource;

    // Java
    requires javax.servlet.api;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // Swagger
    requires io.swagger.v3.core;
    requires io.swagger.v3.jaxrs2;
    requires transitive io.swagger.v3.oas.annotations;
    requires transitive io.swagger.v3.oas.models;
    requires io.swagger.v3.oas.integration;
    requires io.github.classgraph;
    requires java.ws.rs;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.web.swagger;
}

open module kivakit.web.swagger
{
    // KivaKit
    requires transitive kivakit.web.jersey;

    // Swagger
    requires transitive io.swagger.v3.core;
    requires transitive io.swagger.v3.jaxrs2;
    requires transitive io.swagger.v3.oas.annotations;
    requires transitive io.swagger.v3.oas.models;
    requires transitive io.swagger.v3.oas.integration;
    requires transitive io.github.classgraph;
    requires transitive java.ws.rs;

    // Module exports
    exports com.telenav.kivakit.web.swagger;
    exports com.telenav.kivakit.web.swagger.project;
}

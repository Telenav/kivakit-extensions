open module kivakit.web.swagger
{
    // KivaKit
    requires transitive kivakit.web.jersey;

    // Swagger
    requires io.swagger.v3.core;
    requires io.swagger.v3.jaxrs2;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires io.swagger.v3.oas.integration;
    requires transitive io.github.classgraph;
    requires transitive java.ws.rs;
    requires transitive jakarta.activation;

    // Module exports
    exports com.telenav.kivakit.web.swagger;
}

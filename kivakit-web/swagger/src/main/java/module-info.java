open module kivakit.web.swagger
{
    // KivaKit
    requires transitive kivakit.web.jetty;
    requires transitive kivakit.resource;

    // REST
    requires jakarta.ws.rs;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // Swagger
    requires io.swagger.v3.jaxrs2;
    requires io.swagger.v3.oas.integration;

    // Module exports
    exports com.telenav.kivakit.web.swagger;
}

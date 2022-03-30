open module kivakit.web.swagger
{
    // KivaKit
    requires transitive kivakit.web.jetty;
    requires transitive kivakit.resource;

    // Java
    requires javax.servlet.api;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // Swagger
    requires io.swagger.v3.jaxrs2;
    requires io.swagger.v3.oas.integration;
    requires java.ws.rs;

    // Module exports
    exports com.telenav.kivakit.web.swagger;
}
